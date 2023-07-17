package kr.co.realiv.config.security;

import kr.co.realiv.data.User;
import kr.co.realiv.data.dto.DbResponseDto;
import kr.co.realiv.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        DbResponseDto foundUser = userMapper.findByUserId(userId);
        User user = new User(foundUser);

        return new UserDetailsImpl(user, user.getUserId());
    }
}
