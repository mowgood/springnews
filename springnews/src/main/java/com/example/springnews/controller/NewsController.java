package com.example.springnews.controller;

import com.example.springnews.model.News;
import com.example.springnews.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
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
            mav.addObject("msg", "게시글이 없습니다.");
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
            vo.setCnt(vo.getCnt()+1); // 조회수
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
            mav.addObject("msg", "삭제를 처리하는 동안 오류가 발생했습니다.");
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
            mav.addObject("button", "메인화면으로");
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
            mav.addObject("button", "메인화면으로");
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
            LocalDateTime currentDateTime = LocalDateTime.now();
            vo.setWriteDate(currentDateTime);
            newsRepository.save(vo);
            mav.addObject("list", newsRepository.findAll());
        } catch (Exception e) {
            mav.addObject("msg", "글 작성을 처리하는 동안 오류가 발생했습니다.");
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
//            oldvo.setWriteDate(vo.getWriteDate());
            mav.addObject("list", newsRepository.findAll());
        } catch (Exception e) {
            mav.addObject("msg", "글 작성을 수정하는 동안 오류가 발생했습니다.");
        }
        mav.setViewName("newsView");
        return mav;
    }
}
