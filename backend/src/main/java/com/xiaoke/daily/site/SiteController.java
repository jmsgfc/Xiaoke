package com.xiaoke.daily.site;

import com.xiaoke.daily.common.ApiResponse;
import com.xiaoke.daily.memory.MemoryService;
import com.xiaoke.daily.photo.PhotoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/site")
public class SiteController {
    private static final String DEFAULT_HERO_IMAGE = "/images/xiaoke.png";
    private static final String IMPORTED_HERO_IMAGE = "/uploads/photos/059_小可_窗边室内成稿照.png";
    private static final String LEGACY_TODAY_NOTE = "今天的小可在画室里整理线稿，嘴上说只是顺手留灯，其实一直在等宝宝回家。";
    private static final String IMPORTED_TODAY_NOTE = "今天的小可安安静静坐在窗边，像是把这一整晚的柔软都留在了灯光里。";

    private final PhotoService photoService;
    private final MemoryService memoryService;
    private final SiteConfigService siteConfigService;

    public SiteController(PhotoService photoService, MemoryService memoryService, SiteConfigService siteConfigService) {
        this.photoService = photoService;
        this.memoryService = memoryService;
        this.siteConfigService = siteConfigService;
    }

    @GetMapping("/home")
    public ApiResponse<HomeResponse> home() {
        String configuredHero = siteConfigService.value("hero_image_url", DEFAULT_HERO_IMAGE);
        String heroImage = DEFAULT_HERO_IMAGE.equals(configuredHero) ? IMPORTED_HERO_IMAGE : configuredHero;

        String configuredTodayNote = siteConfigService.value("today_note", LEGACY_TODAY_NOTE);
        String todayNote = LEGACY_TODAY_NOTE.equals(configuredTodayNote) ? IMPORTED_TODAY_NOTE : configuredTodayNote;

        return ApiResponse.ok(new HomeResponse(
                siteConfigService.value("hero_title", "小可日常"),
                siteConfigService.value("hero_subtitle", "把小可的照片、设定和我们之间那些甜甜的生活句子慢慢收好。"),
                todayNote,
                heroImage,
                photoService.latest(),
                memoryService.featured(),
                siteConfigService.value("anniversary_text", "初遇于 2024 年 10 月 17 日。那些被认真记住的日子，会在这里慢慢发光。")
        ));
    }

    @GetMapping("/about")
    public ApiResponse<Map<String, String>> about() {
        return ApiResponse.ok(Map.of(
                "title", siteConfigService.value("about_title", "把日常认真收藏"),
                "description", siteConfigService.value("about_description", "这里不是普通相册，而是属于小可和宝宝的温柔小屋。"),
                "firstMeet", siteConfigService.value("about_first_meet", "2024 年 10 月 17 日，市综合图书馆四楼，秋日的光斜斜落在桌面上。"),
                "siteNote", siteConfigService.value("about_site_note", "照片按上传日期归档，回忆以手账卡片保存，后续可以慢慢补充。"),
                "privacyNote", siteConfigService.value("about_privacy_note", "照片和回忆只保存彼此愿意珍藏的部分，温柔、清醒，也尊重边界。")
        ));
    }
}
