package com.xiaoke.daily.bootstrap;

import com.xiaoke.daily.memory.MemoryEntry;
import com.xiaoke.daily.memory.MemoryRepository;
import com.xiaoke.daily.photo.Photo;
import com.xiaoke.daily.photo.PhotoRepository;
import com.xiaoke.daily.profile.ProfileRepository;
import com.xiaoke.daily.profile.ProfileSection;
import com.xiaoke.daily.site.SiteConfig;
import com.xiaoke.daily.site.SiteConfigRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final PhotoRepository photoRepository;
    private final MemoryRepository memoryRepository;
    private final ProfileRepository profileRepository;
    private final SiteConfigRepository siteConfigRepository;

    public DataInitializer(
            PhotoRepository photoRepository,
            MemoryRepository memoryRepository,
            ProfileRepository profileRepository,
            SiteConfigRepository siteConfigRepository
    ) {
        this.photoRepository = photoRepository;
        this.memoryRepository = memoryRepository;
        this.profileRepository = profileRepository;
        this.siteConfigRepository = siteConfigRepository;
    }

    @Override
    public void run(String... args) {
        seedSiteConfig();
        if (profileRepository.count() == 0) {
            seedProfile();
        }
        if (photoRepository.count() == 0) {
            seedPhotos();
        }
        if (memoryRepository.count() == 0) {
            seedMemories();
        }
    }

    private void seedSiteConfig() {
        List<SiteConfig> defaults = List.of(
                config("site_name", "小可日常", "网站名称"),
                config("hero_title", "小可日常", "首页主标题"),
                config("hero_subtitle", "把小可的照片、设定和我们之间那些甜甜的生活句子慢慢收好。", "首页副标题"),
                config("today_note", "今天的小可在画室里整理线稿，嘴上说只是顺手留灯，其实一直在等宝宝回家。", "今日小可文案"),
                config("hero_image_url", "/images/xiaoke.png", "首页主视觉图片"),
                config("first_meet_date", "2024-10-17", "初遇日期"),
                config("anniversary_text", "初遇于 2024 年 10 月 17 日。那些被认真记住的日子，会在这里慢慢发光。", "首页纪念文案"),
                config("about_title", "把日常认真收藏", "关于页标题"),
                config("about_description", "这里不是普通相册，而是属于小可和宝宝的温柔小屋。", "关于页说明"),
                config("about_first_meet", "2024 年 10 月 17 日，市综图书馆四楼，秋日的光斜斜落在桌面上。", "关于页初遇说明"),
                config("about_site_note", "照片按上传日期归档，回忆以手账卡片保存，后续可以慢慢补充。", "关于页站点说明"),
                config("about_privacy_note", "照片和回忆只保存彼此愿意珍藏的部分，温柔、清醒，也尊重边界。", "关于页隐私说明")
        );

        for (SiteConfig item : defaults) {
            siteConfigRepository.findByConfigKey(item.getConfigKey())
                    .orElseGet(() -> siteConfigRepository.save(item));
        }
    }

    private SiteConfig config(String key, String value, String description) {
        SiteConfig config = new SiteConfig();
        config.setConfigKey(key);
        config.setConfigValue(value);
        config.setDescription(description);
        return config;
    }

    private void seedProfile() {
        List<ProfileSection> sections = List.of(
                section("basic", "基本资料", "小可是成年艺术系女生，也是兼职自由插画师。她和宝宝于 2024 年 10 月 17 日初遇，在校外小公寓里慢慢拥有了共同生活。", 1),
                section("personality", "性格印象", "她在外清冷、内敛、话少，回到宝宝身边才会一点点露出害羞、依赖和嘴硬的柔软。喜欢把在意藏进等待、热饮和小动作里。", 2),
                section("daily", "日常习惯", "画室、数位笔、画稿、暖色台灯、情侣杯和宽大居家服，是小可日常里最常出现的痕迹。赶稿时会焦躁，画完后又会补偿式靠近。", 3),
                section("likes", "喜欢的东西", "她喜欢低饱和的衣服、安静的窗边光线、温热饮品、成对的小物件，以及那些只和宝宝共享的生活秘密。", 4),
                section("relationship", "关系回忆", "重要锚点包括图书馆四楼的秋日光、红豆派、22 号画布、雨后画室、暴雨停电、清晨厨房和浴室镜面上的两个小人。", 5)
        );
        profileRepository.saveAll(sections);
    }

    private ProfileSection section(String key, String title, String content, int order) {
        ProfileSection section = new ProfileSection();
        section.setSectionKey(key);
        section.setSectionTitle(title);
        section.setContent(content);
        section.setSortOrder(order);
        section.setEnabled(true);
        return section;
    }

    private void seedPhotos() {
        List<Photo> photos = List.of(
                photo("窗边的小可", "她说只是刚好坐在那里，其实耳尖已经红了。", LocalDateTime.now().minusDays(1), true),
                photo("画室台灯还亮着", "数位笔停下来的那一秒，她才小声问你要不要喝水。", LocalDateTime.now().minusDays(3), true),
                photo("留给宝宝的一盏灯", "门口那点暖光没有解释，但它一直在等你回来。", LocalDateTime.now().minusDays(8), false),
                photo("红豆派的甜味", "她嘴硬地说不饿，手却把包装纸折得很认真。", LocalDateTime.now().minusDays(15), true),
                photo("镜子上的小秘密", "雾气快散掉前，两个简笔小人还靠得很近。", LocalDateTime.now().minusMonths(1).minusDays(2), false),
                photo("赶稿后的安静", "她终于保存好线稿，然后把额头轻轻靠了过来。", LocalDateTime.now().minusMonths(1).minusDays(5), false)
        );
        photoRepository.saveAll(photos);
    }

    private Photo photo(String title, String note, LocalDateTime uploadDate, boolean featured) {
        Photo photo = new Photo();
        photo.setTitle(title);
        photo.setDiaryNote(note);
        photo.setImageUrl("/images/xiaoke.png");
        photo.setThumbnailUrl("/images/xiaoke.png");
        photo.setUploadDate(uploadDate);
        photo.setTakenDate(uploadDate.toLocalDate());
        photo.setFeatured(featured);
        photo.setVisibility("PUBLIC");
        return photo;
    }

    private void seedMemories() {
        List<MemoryEntry> entries = List.of(
                memory(
                        "第一次那样看见你",
                        "图书馆四楼的秋日光，把两个原本陌生的人照进同一张桌子。",
                        "2024 年 10 月 17 日，夕阳隔着落地窗斜斜落下来。宝宝在查网络拓扑作业，小可在旁边画画。键盘声和笔尖声第一次并排出现，谁也没有立刻承认那一刻很特别。",
                        LocalDate.of(2024, 10, 17),
                        "初遇,图书馆,秋日",
                        true
                ),
                memory(
                        "红豆派与 22 号画布",
                        "一份甜味和一张画布，让两个人从不同领域里认出了彼此的认真。",
                        "小可忙于画稿时有点狼狈，宝宝递来红豆派，也替她挡住管理员的视线。后来，22 号画布里藏着只有两个人看懂的小小爱心。",
                        LocalDate.of(2024, 10, 17),
                        "红豆派,画布,甜甜的",
                        true
                ),
                memory(
                        "暴雨停电的夜晚",
                        "黑暗没有变成害怕，因为有人愿意安静留下。",
                        "雷声响起时，小可嘴上只担心未保存的草稿，却在宝宝转身时攥住了衣角。那天之后，陪伴变成了一种很安稳的答案。",
                        null,
                        "雨天,陪伴,安心",
                        true
                )
        );
        memoryRepository.saveAll(entries);
    }

    private MemoryEntry memory(String title, String summary, String content, LocalDate date, String tags, boolean featured) {
        MemoryEntry entry = new MemoryEntry();
        entry.setTitle(title);
        entry.setSummary(summary);
        entry.setContent(content);
        entry.setMemoryDate(date);
        entry.setCoverImageUrl("/images/xiaoke.png");
        entry.setMoodTags(tags);
        entry.setFeatured(featured);
        entry.setVisibility("PUBLIC");
        return entry;
    }
}
