package springblog.myblog.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springblog.myblog.domain.Board;
import springblog.myblog.dto.ResponseDto;
import springblog.myblog.dto.board.BoardDto;
import springblog.myblog.dto.board.BoardPageDto;
import springblog.myblog.dto.board.BoardReplyDto;
import springblog.myblog.service.BoardService;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    // 홈 페이지 (페이징 정보 전송)
    @GetMapping("/api/home")
    public Page<BoardPageDto> home(@PageableDefault(size=3, sort="id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<Board> all = boardService.findAll(pageable);
        return all.map(p -> new BoardPageDto(p.getId(), p.getTitle()));
    }

    // 게시판 생성
    @PostMapping("/api/board/write")
    public ResponseDto<Integer> write(@RequestBody BoardDto boardDto){
        Board board = Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .build();

        boardService.write(board, boardDto.getUser_id());
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    // 특정 게시판 조회
    @GetMapping("/api/board/{id}")
    public BoardDto findBoard(@PathVariable Long id){
        Board findBoard = boardService.findById(id);
        return new BoardDto(findBoard.getTitle(), findBoard.getContent(),findBoard.getCount(),findBoard.getUser().getId());
    }

    // 게시판 정보 + 댓글 정보
    @GetMapping("/api/board/{id}/reply")
    public BoardReplyDto findBoardWithReply(@PathVariable Long id){
        Board board = boardService.findBoardWithUser(id);
        return BoardReplyDto.builder()
                .board_id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .user_id(board.getUser().getId())
                .username(board.getUser().getUsername())
                .replies(board.getReplies().stream()
                        .map(BoardReplyDto::convertDto)
                        .collect(Collectors.toList()))
                .build();
    }


    // 게시판 수정
    @PatchMapping("/api/board/update/{id}")
    public ResponseDto<Integer> update(@RequestBody BoardDto boardDto, @PathVariable Long id){
        boardService.update(id, boardDto.getTitle(), boardDto.getContent());
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    // 게시글 삭제
    @DeleteMapping("/api/board/delete/{id}")
    public ResponseDto<Integer> delete(@PathVariable Long id){
        try{
            boardService.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), 0);
        }
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }
}
