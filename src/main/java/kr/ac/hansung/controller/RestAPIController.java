package kr.ac.hansung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import kr.ac.hansung.model.User;
import kr.ac.hansung.service.UserService;

@RestController //@Controller + @ResponseBody
@RequestMapping("/api")
public class RestAPIController {
	@Autowired
	UserService userService;
	
	// --- Retrieve ALL Users 모든 사용자 조회
	//users로 매핑 get메서드 사용
	//ResponseEntity : header, body, HTTP status 3가지를 다 저장해서
	//				   json 포맷으로 바꿔서 넘겨줌
	@RequestMapping(value="/users", method=RequestMethod.GET)
	public ResponseEntity<List<User>> listAllUsers(){
		List<User> users = userService.findAllUsers();
		if(users.isEmpty()) {
			//사용자 정보가 없을 경우 Http Status에 NO_CONTENT를 담은 객체 생성
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		//사용자 정보가 존재한다면 body에 user정보와 http status에는 ok를 넣어서 넘겨줌
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
	//한 사용자 조회
	@RequestMapping(value="/users/{id}", method=RequestMethod.GET)
	public ResponseEntity<User> getUser(@PathVariable("id") long id) {
		
		User user = userService.findById(id);
		if (user == null) {
			
		}
		//사용자 정보가 존재한다면 body에 user정보와 http status에는 ok를 넣어서 넘겨줌
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	//사용자 생성
	//body는 보내지 않고 header에만 정보를 넣어서 전송
	//post : request body부분에 json포멧 넘어옴 -> 바인딩 시키기 위해 @RequestBody 사용
	@RequestMapping(value="/users", method=RequestMethod.POST)
	//@RequestBody : json을 객체로 바인딩 시켜줌
	public ResponseEntity<Void> createUser(@RequestBody User user,
			UriComponentsBuilder ucBuilder){ //사용자의 uri header에 담기위해 사용
		
		if(userService.isUserExist(user)) {
			
			
		}
		
		userService.saveUser(user);
		
		HttpHeaders headers = new HttpHeaders();
		//header에 location path를 uri로 바껴서 id에 넣어 헤더에 담김
		headers.setLocation(ucBuilder.path("/api/users/{id}").
				buildAndExpand(user.getId()).toUri());
		//저장한 후 header에 사용자 정보를 넘겨주며 http status에는 created를 넣어 넘겨줌
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	//업데이트
	@RequestMapping(value="/users/{id}", method=RequestMethod.PUT)
	//@RequestBody : json을 객체로 바인딩 시켜줌
	public ResponseEntity<User> updateUser(@PathVariable("id") long id,
			@RequestBody User user){
		
		User currentUser = userService.findById(id);
		
		if(currentUser == null) {
			
		}
		
		currentUser.setName(user.getName());
		currentUser.setAge(user.getAge());
		currentUser.setSalary(user.getSalary());
		
		
		userService.updateUser(currentUser);
		
		return new ResponseEntity<User>(currentUser, HttpStatus.OK);
	}
	
	//삭제
	@RequestMapping(value="/users/{id}", method=RequestMethod.DELETE)
	//@RequestBody : json을 객체로 바인딩 시켜줌
	public ResponseEntity<User> deleteUser(@PathVariable("id") long id){
		
		User user = userService.findById(id);
		if(user == null) {
			
		}
		
		userService.deleteUserById(id);

		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}
	
	//다 삭제
	@RequestMapping(value="/users", method=RequestMethod.DELETE)
	//@RequestBody : json을 객체로 바인딩 시켜줌
	public ResponseEntity<User> deleteAllUsers(){
		
		userService.deleteAllUsers();

		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}
	
}
