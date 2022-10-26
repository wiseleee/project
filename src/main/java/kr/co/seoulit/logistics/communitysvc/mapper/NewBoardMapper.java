package kr.co.seoulit.logistics.communitysvc.mapper;

import kr.co.seoulit.logistics.communitysvc.to.NewBoardTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Mapper
@Transactional
public interface NewBoardMapper {

	ArrayList<NewBoardTO> selectNewBoardList();
	void insertNewBoard(NewBoardTO newBoard);
	NewBoardTO selectNewBoardDetail(int id);
	void deleteNewBoard(int id);
	void updateNewBoard(NewBoardTO newBoard);
}
