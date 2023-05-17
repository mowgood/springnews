package com.example.springnews.controller;

import com.example.springnews.model.News;
import com.example.springnews.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.util.List;

@Controller
public class NewsController {
    @Autowired
    NewsRepository newsRepository;

    @GetMapping("/newsmain")
    public ModelAndView list() {
        List<News> list = newsRepository.findAll();
        ModelAndView mav = new ModelAndView();
        if (list.size() != 0) {
            mav.addObject("list", list);
        } else {
            mav.addObject("msg", "작성된 게시글이 없습니다.");
        }
        mav.setViewName("newsView");
        return mav;
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
    public ModelAndView delete(int id) {
        ModelAndView mav = new ModelAndView();
        try {
            newsRepository.deleteById(id);
            mav.addObject("list", newsRepository.findAll());
        } catch (Exception e) {
            mav.addObject("errorMsg", "삭제를 처리하는 동안 오류가 발생했습니다.");
        }
        mav.setViewName("newsView");
        return mav;
    }

    @GetMapping("/search")
    public ModelAndView search(String keyword) {
        List<News> list = newsRepository.findByContentContains(keyword);
        ModelAndView mav = new ModelAndView();
        if (list.size() != 0) {
            mav.addObject("list", list);
        } else {
            mav.addObject("msg", "게시물을 찾을 수 없습니다.");
        }
        mav.setViewName("newsView");
        return mav;
    }

    @GetMapping("/writer")
    public ModelAndView searchWriter(String writer) {
        List<News> list = newsRepository.findByWriter(writer);
        ModelAndView mav = new ModelAndView();
        if (list.size() != 0) {
            mav.addObject("list", list);
        } else {
            mav.addObject("msg", "게시물을 찾을 수 없습니다.");
        }
        mav.setViewName("newsView");
        return mav;
    }

    @PostMapping("/insert")
    @Transactional
    public ModelAndView insert(News vo) {
        System.out.println(vo);
        ModelAndView mav = new ModelAndView();
        try {
            newsRepository.save(vo);
            mav.addObject("list", newsRepository.findAll());
        } catch (Exception e) {
            mav.addObject("errorMsg", "글 작성을 처리하는 동안 오류가 발생했습니다.");
        }
        mav.setViewName("newsView");
        return mav;
    }

    @PostMapping("/update")
    @Transactional
    public ModelAndView update(News vo) {
        System.out.println("update");
        ModelAndView mav = new ModelAndView();
        try {
            News oldvo = newsRepository.findById(vo.getId()).get();
            oldvo.setWriter(vo.getWriter());
            oldvo.setTitle(vo.getTitle());
            oldvo.setContent(vo.getContent());
            mav.addObject("list", newsRepository.findAll());
        } catch (Exception e) {
            mav.addObject("errorMsg", "글 작성을 수정하는 동안 오류가 발생했습니다.");
        }
        mav.setViewName("newsView");
        return mav;
    }

    @GetMapping("/part")
    public ModelAndView part(int page, int size) {
        ModelAndView mav = new ModelAndView();
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<News> pageObj = newsRepository.findAll(pageRequest);
        List<News> list = pageObj.toList();
        int totalPage = pageObj.getTotalPages();
        mav.setViewName("newsView");
        mav.addObject("list", list);
        mav.addObject("totalPage", totalPage);
        return mav;
    }
}
