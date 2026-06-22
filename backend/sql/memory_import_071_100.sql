-- MySQL dump 10.13  Distrib 9.5.0, for Win64 (x86_64)
--
-- Host: localhost    Database: xiaoke_daily
-- ------------------------------------------------------
-- Server version	9.5.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `memory_entry`
--
-- WHERE:  id IN (26,27,28,29,30,31,32,33)

LOCK TABLES `memory_entry` WRITE;
/*!40000 ALTER TABLE `memory_entry` DISABLE KEYS */;
REPLACE INTO `memory_entry` (`id`, `title`, `summary`, `content`, `memory_date`, `cover_image_url`, `cover_storage_path`, `mood_tags`, `is_featured`, `visibility`, `sort_order`, `created_at`, `updated_at`, `favorite_count`) VALUES (26,'画室里慢慢亮起来的下午','她安静画画的时候，连风都不舍得打扰。','那天下午的光很好，画室里没有太多声音，只有笔尖和屏幕偶尔亮起的细碎动静。她低头画画的时候很专注，像把整个人都放进了那幅画里。我没有说太多话，只是坐在旁边看她一点点把线条补完整。后来想起那一幕，最先记住的不是画面本身，而是她安静下来以后，周围一切都跟着柔和了。','2026-06-07','/uploads/photos/074_小可_白天画室数位板作画.png','photos/074_小可_白天画室数位板作画.png','画室,安静,日常,陪伴',0,'PUBLIC',0,'2026-06-12 01:57:47','2026-06-12 01:57:47',NULL),(27,'灯亮着，就像有人在等','房间的灯一亮，晚上的心也跟着安稳下来。','其实只是很普通的一个夜晚，可一盏暖灯把房间照亮以后，回家的感觉就突然具体了。桌角、床边、留在原地的小物件，都像在说今天也被好好生活过。有时候让人安心的，不一定是什么大事，可能只是推门那一刻，知道有人把光留着。我把这张留在手账里，是因为它真的很像我们想要的那种生活。','2026-06-09','/uploads/photos/083_小可_夜晚房间全景暖光版.png','photos/083_小可_夜晚房间全景暖光版.png','房间,夜晚,暖光,安心',1,'PUBLIC',0,'2026-06-12 01:57:47','2026-06-12 01:57:47',NULL),(28,'床边那一点安稳','没说出口的疲惫，被夜晚和床铺一起轻轻接住了。','那天并没有发生什么特别的事，只是回到房间以后，看到整理好的床铺，心里忽然松了一点。灯光很轻，被子软软地搭着，像夜晚提前把休息的理由准备好了。后来我才发现，原来很多被记住的片段，都不是热闹的，是这种让人终于能慢下来的一小会儿。','2026-06-09','/uploads/photos/086_小可_卧室床铺夜晚空镜.png','photos/086_小可_卧室床铺夜晚空镜.png','卧室,放松,夜晚,安稳',0,'PUBLIC',0,'2026-06-12 01:57:47','2026-06-12 01:57:47',NULL),(29,'洗完头发以后','她湿着头发坐在床边，整个人都软下来了一点。','刚洗完澡的时候，她总会比平时安静一些。湿发还垂在肩上，人坐在床边，像把白天的疲惫一起洗掉了。我很喜欢这种没有刻意摆拍的瞬间，松松散散，带一点困意，也带一点只属于晚上的温柔。有时候喜欢一个人，就是会把这样很轻的小画面也记很久。','2026-06-10','/uploads/photos/089_小可_湿发床上坐姿照.png','photos/089_小可_湿发床上坐姿照.png','夜晚,温柔,居家,湿发',0,'PUBLIC',0,'2026-06-12 01:57:47','2026-06-12 01:57:47',NULL),(30,'夜晚想靠近你','走在街上时轻轻靠过来的那一下，心会立刻软掉。','街边灯光并不特别亮，可她靠过来的时候，周围一下就安静了。只是挽住手臂、站近一点这么简单的动作，却比很多热闹场面都更让人心动。后来回想那晚，真正留下来的不是街景，是她在身边时那种很实在的靠近感。原来喜欢真的会让夜风都变得轻一点。','2026-06-10','/uploads/photos/091_小可_夜晚街边依偎看镜头.png','photos/091_小可_夜晚街边依偎看镜头.png','夜晚,依偎,街边,心动',1,'PUBLIC',0,'2026-06-12 01:57:47','2026-06-12 01:57:47',NULL),(31,'今天也认真练习了','不吵不闹地坚持着，认真本身就已经很可爱。','训练的时候她总是很安静，不会故意表现什么，只是把动作一遍遍做完。那种专注有点倔，也有点可爱，让人忍不住想替她记下来。有些喜欢并不是因为惊艳，而是因为看见她在平常的日子里，一直认真地对待自己。这样的瞬间放进手账里，会让回头看的时候也多一点力量。','2026-06-10','/uploads/photos/093_小可_健身房绳索训练照.png','photos/093_小可_健身房绳索训练照.png','训练,认真,日常,坚持',0,'PUBLIC',0,'2026-06-12 01:57:47','2026-06-12 01:57:47',NULL),(32,'把花画完的晚上','最后一笔落下去的时候，像把整天的心事也轻轻放好了。','那晚她对着花稿坐了很久，线条、明暗、颜色一点点慢慢收拢。直到最后终于停下来，整个人才像松了口气。我很喜欢这种完成一件事后的安静时刻，没有喧闹，只有一种很淡却很真实的满足。认真生活的人，连收尾的样子都值得被记下来。','2026-06-11','/uploads/photos/097_小可_花卉素描成稿照.png','photos/097_小可_花卉素描成稿照.png','插画,完成,花朵,夜晚',0,'PUBLIC',0,'2026-06-12 01:57:47','2026-06-12 01:57:47',NULL),(33,'和熊猫出去玩的那天','难得闹腾地摆了个姿势，整天的心情都被带亮了。','那天出门本来只是随便逛逛，结果在熊猫雕像前她忽然认真摆起姿势来。平时偏安静的人，偶尔这样配合镜头，反而会让人一下子开心很久。我把这天记下来，不是因为去了多特别的地方，而是她难得把轻松和俏皮都摆在了脸上。有些回忆就是这样，看一眼就会想再笑一次。','2026-06-11','/uploads/photos/100_小可_熊猫雕像前摆拍照.png','photos/100_小可_熊猫雕像前摆拍照.png','出门,散步,轻松,可爱',1,'PUBLIC',0,'2026-06-12 01:57:47','2026-06-12 01:57:47',NULL);
/*!40000 ALTER TABLE `memory_entry` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-17  0:17:08
