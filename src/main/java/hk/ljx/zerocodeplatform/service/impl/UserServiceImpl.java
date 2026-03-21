package hk.ljx.zerocodeplatform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import hk.ljx.zerocodeplatform.constant.UserConstant;
import hk.ljx.zerocodeplatform.exception.BusinessException;
import hk.ljx.zerocodeplatform.exception.ErrorCode;
import hk.ljx.zerocodeplatform.exception.ThrowUtils;
import hk.ljx.zerocodeplatform.model.dto.UserQueryRequest;
import hk.ljx.zerocodeplatform.model.entity.User;
import hk.ljx.zerocodeplatform.mapper.UserMapper;
import hk.ljx.zerocodeplatform.model.enums.UserRoleEnum;
import hk.ljx.zerocodeplatform.model.vo.LoginUserVO;
import hk.ljx.zerocodeplatform.model.vo.UserVO;
import hk.ljx.zerocodeplatform.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static hk.ljx.zerocodeplatform.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户 服务层实现。
 *
 * @author fish
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        // 2. 检查是否重复
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.mapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        // 3. 加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 4. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("无名");
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return user.getId();
    }

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request      请求
     * @return 脱敏后的用户
     */
    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        ThrowUtils.throwIf(userAccount.length() < 4, ErrorCode.PARAMS_ERROR, "账号错误");
        ThrowUtils.throwIf(userPassword.length() < 8, ErrorCode.PARAMS_ERROR, "密码错误");
        // 加密
        userPassword = getEncryptPassword(userPassword);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", userPassword);
        User user = this.mapper.selectOneByQuery(queryWrapper);
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR, "用户不存在");
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    /**
     * 获取已登陆用户信息
     * @param request request请求
     * @return 用户信息
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        ThrowUtils.throwIf(user==null||user.getId()==null, ErrorCode.PARAMS_ERROR, "未登录");
        user = this.mapper.selectOneById(user.getId());
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR, "未登录");
        return user;
    }

    /**
     * 用户退出登录
     * @param request request请求
     * @return 退出登录结果
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 判断用户是否登陆
        User user = this.getLoginUser(request);
        ThrowUtils.throwIf(user==null, ErrorCode.PARAMS_ERROR, "未登录");
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    /**
     * 用户脱敏
     *
     * @param user 脱敏前用户
     * @return 脱敏后的用户
     */
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    /**
     * 用户脱敏
     *
     * @param user 脱敏前用户
     * @return 脱敏后的用户
     */
    @Override
    public UserVO getUserVO(User user) {
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR, "用户不存在");
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 获取脱敏用户列表
     *
     * @param userList 脱敏前用户列表
     * @return 脱敏后的用户列表
     */
    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR, "查询参数不能为空");

        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userAccount = userQueryRequest.getUserAccount();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();

        return QueryWrapper.create()
                .eq(User::getId, id)
                .like(User::getUserName, userName)
                .like(User::getUserAccount, userAccount)
                .like(User::getUserProfile, userProfile)
                .eq(User::getUserRole, userRole)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }

    /**
     * 获取加密密码
     *
     * @param userPassword 密码
     * @return 加密后的密码
     */
    @Override
    public String getEncryptPassword(String userPassword) {
        // 盐值，混淆密码
        final String SALT = "fish";
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }

}
