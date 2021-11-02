package com.p1.service;

//@Service("userDetailService")
//public class UserDetailService implements UserDetailsService {
//    @Autowired
//    private UserDao userDao;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserInfo userInfo = userDao.findByUsername(username);
//        //处理自己的UserInfo对象封装成Userdetails的实现类对象user
//        //注意下面给的User导包是 import org.springframework.security.core.userdetails.User;
//        User user=new User(userInfo.getUsername(),userInfo.getPassword(),getAuthority(userInfo.getRoles()));
//        return user;
//    }
//    /**
//     * 返回一个集合，目的是拿到用户权限
//     * @param roles
//     * @returno
//     */
//    public List<SimpleGrantedAuthority> getAuthority(List<Role> roles){
//        List<SimpleGrantedAuthority> list=new ArrayList<>();
//        for (Role role : roles) {
//            list.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
//        }
//        return list;
//    }
//}
