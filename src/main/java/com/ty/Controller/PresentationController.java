package com.ty.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ty.Entities.Presentation;
import com.ty.Service.PresentationService;
import com.ty.dto.UpdateRequest;


@RestController
@RequestMapping("/Presentation")
public class PresentationController {
	@Autowired
	private PresentationService service;

	
	@PostMapping("/AddStudentDetails")
	public ResponseEntity<?> add(@RequestParam Integer adminId, @RequestParam Integer Userid,@RequestBody Presentation presentation) {
	    ResponseEntity<?> userDTO = service.add(adminId, Userid, presentation);
	    return ResponseEntity.ok(userDTO);
	}


	@GetMapping("/fetch")
	
	public ResponseEntity<?> fetch(@RequestParam Integer pid){
		return service.fetch(pid);
	}
	
	@GetMapping("/FetchAll")
	public ResponseEntity<?> fetchAll(@RequestParam Integer uid){
		return service.fetchall(uid);
	}
	
	@PostMapping("/ChangeStatus")
	public ResponseEntity<?> changestatus(@RequestParam Integer uid ,@RequestParam Integer pid , @RequestBody String Status){
		return service.changesstatus(uid,pid,Status);
	}

    @PutMapping("/score")
    public ResponseEntity<?> updatePresentationScore(@RequestParam Integer aid, @RequestParam Integer pid,@RequestBody UpdateRequest request) {
    	return service.updatePresentationScore(aid, pid,request);
    }
}
