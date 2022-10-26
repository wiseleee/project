package kr.co.seoulit.logistics.communitysvc.service;

import kr.co.seoulit.logistics.communitysvc.to.NewBoardTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public interface NewBoardService {

	public ArrayList<NewBoardTO> findNewBoardList();
	public void registerNewBoard(NewBoardTO newBoard);
	public NewBoardTO findNewBoardDetail(int id);
	public void removeNewBoard(int id);
	public void modifyNewBoard(NewBoardTO newBoard);
}
