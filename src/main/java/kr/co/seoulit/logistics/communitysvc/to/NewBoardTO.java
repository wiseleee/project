package kr.co.seoulit.logistics.communitysvc.to;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NewBoardTO {
	//private int num_seq;
	private int id;          // 게시글번호
	private String nickname;	// 작성자
	private String askdate;    // 작성일자
	private String title;		// 제목
	private String content;		// 내용

}
