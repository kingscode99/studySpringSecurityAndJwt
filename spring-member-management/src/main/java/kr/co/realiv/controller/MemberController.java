package kr.co.realiv.controller;

import kr.co.realiv.config.security.UserDetailsImpl;
import kr.co.realiv.data.dto.DbResponseDto;
import kr.co.realiv.data.dto.ResponseDto;
import kr.co.realiv.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member-list")
    public ResponseEntity<ResponseDto> getMemberList() {
        return ResponseEntity.ok(new ResponseDto(memberService.getMemberList()));
    }

    @GetMapping("/member-details")
    public ResponseEntity<ResponseDto> getMemberDetail(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new ResponseDto(memberService.getMemberDetails(userDetails)));
    }
}
