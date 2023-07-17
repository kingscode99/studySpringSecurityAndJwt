package kr.co.realiv.mapper;

import kr.co.realiv.data.User;
import kr.co.realiv.data.dto.DbResponseDto;
import kr.co.realiv.data.dto.UserUpdateAndDeleteDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    DbResponseDto findByUserId(String userId);

    void signUp(@Param("params") User params);

    List<DbResponseDto> getMemberList();

    List<DbResponseDto> getAllMemberDetails();

    DbResponseDto getMemberDetails(String userId);

    void updateUserId(@Param("params") UserUpdateAndDeleteDto params);

    void deleteUser(@Param("params") UserUpdateAndDeleteDto params);

}
