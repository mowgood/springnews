package com.example.springnews.controller;

import com.example.springnews.model.News;
import com.example.springnews.model.PageDTO;
import com.example.springnews.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.util.List;

@Controller
public class NewsController {
    @Autowired
    NewsRepository newsRepository;

    @GetMapping("/newsmain")
    public String list(@RequestParam(value = "page", defaultValue="0") int page, @RequestParam(value="size", defaultValue = "5") int size,
                       Model model) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<News> pageObj = newsRepository.findAll(pageRequest);
        List<News> list = pageObj.toList();
        int totalPage = pageObj.getTotalPages();
        int pageNum = 5;
        boolean prev = pageObj.hasPrevious();
        boolean next = pageObj.hasNext();
        int startPage = (int)((Math.floor(page/pageNum)*pageNum)+1 <= totalPage ? (Math.floor(page/pageNum)*pageNum)+1 : totalPage);
        int endPage = (startPage + pageNum-1 < totalPage ? startPage + pageNum-1 : totalPage);

        model.addAttribute("list", list);
        model.addAttribute("pageDTO", new PageDTO(totalPage, startPage, endPage, page+1, size, prev, next));

        return "newsView";
    }

    @GetMapping(value = "/listOne", produces = "application/json; charset=utf-8")
    @ResponseBody
    @Transactional
    public News one(int id) {
        News vo = null;
        try {
            vo = newsRepository.findById(id).get();
            vo.setCnt(vo.getCnt()+1);
        } catch (Exception e) {
            return vo;
        }

        return vo;
    }

    @GetMapping("/delete")
    @Transactional
    public String delete(int id, Model model) {
        try {
            newsRepository.deleteById(id);
            model.addAttribute("list", newsRepository.findAll());
        } catch (Exception e) {
            model.addAttribute("errorMsg", "삭제를 처리하는 동안 오류가 발생했습니다.");
        }

        return "redirect:/newsmain";
    }

    @GetMapping("/search")
    public String search(String keyword, Model model) {
        List<News> list = newsRepository.findByContentContains(keyword);

        if (list.size() != 0) {
            model.addAttribute("list", list);
        } else {
            model.addAttribute("msg", "게시물을 찾을 수 없습니다.");
        }
        model.addAttribute("searchKeyword", keyword);
        model.addAttribute("searchMsg", "검색 결과");

        return "newsView";
    }

    @GetMapping("/writer")
    public String searchWriter(String writer, Model model) {
        List<News> list = newsRepository.findByWriter(writer);

        if (list.size() != 0) {
            model.addAttribute("list", list);
        } else {
            model.addAttribute("msg", "게시물을 찾을 수 없습니다.");
        }
        model.addAttribute("searchKeyword", writer);
        model.addAttribute("searchMsg", "검색 결과");

        return "newsView";
    }

    @PostMapping("/insert")
    @Transactional
    public String insert(News vo, Model model) {
        try {
            newsRepository.save(vo);
            model.addAttribute("list", newsRepository.findAll());
        } catch (Exception e) {
            model.addAttribute("errorMsg", "글 작성을 처리하는 동안 오류가 발생했습니다.");
        }

        return "redirect:/newsmain";
    }

    @PostMapping("/update")
    @Transactional
    public String update(News vo, Model model) {
        try {
            News oldvo = newsRepository.findById(vo.getId()).get();
            oldvo.setWriter(vo.getWriter());
            oldvo.setTitle(vo.getTitle());
            oldvo.setContent(vo.getContent());
            model.addAttribute("list", newsRepository.findAll());
        } catch (Exception e) {
            model.addAttribute("errorMsg", "글 작성을 수정하는 동안 오류가 발생했습니다.");
        }

        return "redirect:/newsmain";
    }

}
