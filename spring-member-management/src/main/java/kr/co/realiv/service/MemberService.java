package kr.co.realiv.service;

import kr.co.realiv.config.security.UserDetailsImpl;
import kr.co.realiv.data.dto.DbResponseDto;
import kr.co.realiv.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final UserMapper mapper;

    public List<DbResponseDto> getMemberList() {
        return mapper.getMemberList();
    }

    public List<DbResponseDto> getMemberDetails(UserDetailsImpl userDetails) {
        if (userDetails.isAdmin()) {
            return mapper.getAllMemberDetails();
        }
        List<DbResponseDto> memberDetails = new ArrayList<>();
        memberDetails.add(mapper.getMemberDetails(userDetails.getUser().getUserId()));
        return memberDetails;
    }
}
