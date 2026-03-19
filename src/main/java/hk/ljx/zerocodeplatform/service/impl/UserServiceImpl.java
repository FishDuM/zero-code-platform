package hk.ljx.zerocodeplatform.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import hk.ljx.zerocodeplatform.model.entity.User;
import hk.ljx.zerocodeplatform.mapper.UserMapper;
import hk.ljx.zerocodeplatform.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户 服务层实现。
 *
 * @author fish
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService {

}
