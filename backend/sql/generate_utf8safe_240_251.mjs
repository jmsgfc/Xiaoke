import fs from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";

const __filename = fileURLToPath(import.meta.url);
const sqlDir = path.dirname(__filename);
const projectRoot = path.resolve(sqlDir, "../..");
const photoDir = path.join(projectRoot, "uploads/photos");

const photoOut = path.join(sqlDir, "photo_import_240_251.sql");
const memoryOut = path.join(sqlDir, "memory_import_240_251.sql");
const photoUtf8Out = path.join(sqlDir, "photo_import_240_251_utf8safe.sql");
const memoryUtf8Out = path.join(sqlDir, "memory_import_240_251_utf8safe.sql");

const sqlText = (value) => `'${String(value).replace(/'/g, "''")}'`;
const hexExpr = (text) =>
  `CONVERT(0x${Buffer.from(text, "utf8").toString("hex").toUpperCase()} USING utf8mb4)`;

const resolveFile = (id) => {
  const prefix = `${String(id).padStart(3, "0")}_`;
  const found = fs.readdirSync(photoDir).find((name) => name.startsWith(prefix));
  if (!found) throw new Error(`Photo file not found for id ${id}`);
  return found;
};

const photos = [
  [1900, "走廊里停下来回消息", "她站在走廊里低头看手机，宽松白上衣把整个人衬得很轻，像刚刚从安静的一天里走出来。", 240, "2026-06-30", 1, "2026-07-01 10:01:00"],
  [1910, "紫藤花旁的白卫衣", "她站在紫藤花旁边，白卫衣和豹纹短裙意外地很搭，像把春天的尾巴轻轻接住了。", 241, "2026-06-30", 0, "2026-07-01 10:02:00"],
  [1920, "书桌上的几何素描", "纸张、笔记和耳机都摊在桌上，像把这阵子认真画画的痕迹一股脑留了下来。", 242, "2026-06-30", 0, "2026-07-01 10:03:00"],
  [1930, "镜前的棕色背心", "她在镜前随手拍下一张，棕色背心和黑短裙把今天穿得很利落。", 243, "2026-06-30", 0, "2026-07-01 10:04:00"],
  [1940, "超市里的一点粉色", "在超市货架旁边站一下，粉色上衣和蛋糕裙把普通出门也拍得很可爱。", 244, "2026-06-30", 0, "2026-07-01 10:05:00"],
  [1950, "居家的黑白洛丽塔", "她在家里换上黑白洛丽塔，安静站着的时候，像把可爱也认真整理好了。", 245, "2026-06-30", 1, "2026-07-01 10:06:00"],
  [1960, "夜晚街头的黑色外套", "夜色把街边的灯拉得很长，她穿着黑色运动外套站在那里，像把情绪也收得很稳。", 246, "2026-06-30", 0, "2026-07-01 10:07:00"],
  [1970, "和室里的侧身一眼", "巫女服在木色和纸门之间很安静，她只是侧身站着，就已经很像一页故事。", 247, "2026-06-30", 1, "2026-07-01 10:08:00"],
  [1980, "夜里路过的白T", "白T和牛仔短裤在夜色里显得很轻，像出门散一会儿步时顺手留下的纪念。", 248, "2026-07-01", 0, "2026-07-01 10:09:00"],
  [1990, "蓝色蕾丝上衣的居家感", "她在家里穿着蓝色蕾丝上衣和短裤，整个人都很放松，像把夏天留在了房间里。", 249, "2026-07-01", 0, "2026-07-01 10:10:00"],
  [2000, "桥边晚风吹过来的时候", "她站在桥边，蓝边无袖上衣被夜风吹得很轻，城市灯光都成了很远的背景。", 250, "2026-07-01", 1, "2026-07-01 10:11:00"],
  [2010, "紫藤花下再站一会儿", "她站在紫藤花旁边低头不说话，白卫衣和短裙把这阵风景衬得很像初夏。", 251, "2026-07-01", 1, "2026-07-01 10:12:00"],
];

const memories = [
  [
    490,
    "书桌上摊开的几何练习",
    "纸张、笔记和线稿挤在一起，像把这阵子埋头画画的认真都摊开给人看了。",
    `那天的书桌有一点乱，却不是慌张的乱。

几何素描本、草稿纸、笔记本、耳机和平台都摊在桌上，像每一样东西都刚刚参与过一段认真安静的时间。画纸上那些棱角和阴影画得很稳，旁边的笔记又把过程记得很细，连桌边那杯子和小收纳盒都像在证明，这不是一张为了拍照整理过的桌面，而是很真实的一段日常。

我很喜欢这种照片，因为它记录的不是“完成”，而是正在发生的状态。不是作品挂起来的时候，也不是摆拍得最整齐的时候，而是她刚刚画完、刚刚停笔、刚刚从专注里抬起头来的那个片刻。桌面看起来有一点满，可心情反而很安静。

以后再翻到这一页，我大概会先想起她伏在桌前一笔一笔把立体关系画清楚的样子，再想起这些看似普通的工具和纸张，其实就是她生活里最稳定也最温柔的陪伴。`,
    242,
    "2026-06-30",
    "书桌,素描,日常",
    1,
  ],
  [
    500,
    "在家里换上黑白洛丽塔的晚一会",
    "她站在房间里，黑白裙摆一层一层落下来，像把平常的夜晚也认真变成了小小舞台。",
    `那天其实没有什么特别大的事情，只是回到家以后，她换上了那套黑白洛丽塔。

房间还是平常的房间，柜子、门框和旁边的小植物都没有变，可衣服一换上，整个画面就忽然有了另一种节奏。黑白配色把人衬得很干净，裙摆和蕾丝又把可爱收得很完整。她安安静静站在那里，没有故意摆出很夸张的表情，却已经足够让人记很久。

我会想把这张放进手账里，是因为它很像生活里突然出现的一点惊喜。不是出门，不是活动现场，也不是被谁围着看的时候，而是在最熟悉的居家背景里，她认真穿上自己喜欢的样子，让普通的一晚忽然变得值得纪念。

这种照片很像在提醒人，生活感和仪式感其实并不冲突。一个平平常常的夜里，也可以因为一个喜欢的穿搭、一点认真对待自己的心情，而慢慢发亮。`,
    245,
    "2026-06-30",
    "居家,洛丽塔,晚间",
    1,
  ],
  [
    510,
    "和室里像故事一样的侧影",
    "木色、纸门和巫女服把画面压得很静，她只是侧身站着，就已经像一段会被慢慢记住的情节。",
    `和室的光线总是比别的地方更慢一点。

她穿着巫女服站在窗边，白衣红裙被木色背景衬得很稳，整个人像从一页旧故事里走出来。没有太多动作，也没有热闹的表情，只是安安静静把身体停在那束光旁边，画面就自己慢慢完整起来了。

我很喜欢这种有点“离现实远一点”的瞬间。明明还是她，还是熟悉的眼神和站姿，可场景、衣服和光一合起来，就会让人自然地想给它添上一点前后文。像是下一秒她会转身，或者会顺着窗边再往前走一点，让整段故事继续展开。

这张很适合留在回忆手账里，因为它不是那种一眼热闹的照片，而是越看越安静、越看越有氛围的画面。像很多以后会被反复想起的记忆一样，不靠声音取胜，只靠那一点恰到好处的停顿。`,
    247,
    "2026-06-30",
    "和室,巫女服,安静",
    1,
  ],
  [
    520,
    "桥边晚风吹得人很软",
    "她站在桥边，城市的灯和河面的光都退到身后，只剩夜风把那一刻慢慢托住。",
    `有些夜晚并不需要安排得多特别，只要走到桥边站一会儿，就已经足够让人安静下来。

她穿着浅色上衣站在栏杆旁边，远处是散开的城市灯光，身后有水，也有夜色把画面压得很深。她没有正对镜头，只是把目光落在别处，像正在想事情，又像只是单纯在吹风。那种不急着说话的神情，会让整个画面都跟着慢下来。

我会特别想记住这一张，是因为它把“夜晚的松弛感”拍得很完整。不是灯火特别热闹，也不是姿势特别精致，而是她站在那里时，周围的一切都刚好退成背景，只留下一个很轻、很稳、很适合被夜风吹一会儿的人。

以后翻到这里，大概会想起那种城市明明还在发光，可人已经慢慢安静下来的感觉。不是逃开热闹，只是终于有一小段时间，可以把心放回自己身上。`,
    250,
    "2026-07-01",
    "桥边,夜晚,晚风",
    1,
  ],
  [
    530,
    "紫藤花旁停住的初夏",
    "白卫衣、短裙和垂下来的紫藤花把这一刻衬得很轻，像初夏忽然愿意为她多停一会儿。",
    `紫藤花开的季节，总会让普通的小路也变得像专门为某个人准备好的背景。

她站在花旁边，白卫衣宽宽松松地垂下来，短裙和球鞋又把整个人衬得很轻。没有特别强烈的表情，也没有刻意摆出来的热闹动作，只是低头站在那里，风景和她就自然地贴在了一起。紫色花串垂下来，周围全是干净的绿，连光都像比平时更柔一点。

我喜欢这组照片，不只是因为画面好看，而是因为它很像“季节真的来过”的证据。很多时候初夏不是很响亮地到来，而是像这样，在树荫底下、在花旁边、在一个人安静站着的时候，被慢慢看见。

这张放进手账里很合适，因为它本身就像一段季节性的回忆。以后再看，未必先想起那天说了什么、去了哪里，反而会先想起紫藤花垂下来时的光线，和她站在花旁边那种一点也不着急的样子。`,
    251,
    "2026-07-01",
    "紫藤花,初夏,散步",
    0,
  ],
];

const buildPhotoPlain = () => {
  const lines = [
    "SET NAMES utf8mb4;",
    "USE `xiaoke_daily`;",
    "",
    "DELETE FROM `photo`",
    "WHERE `storage_path` REGEXP '^photos/(24[0-9]|25[0-1])_';",
    "",
    "INSERT INTO `photo`",
    "(`title`, `diary_note`, `image_url`, `thumbnail_url`, `storage_path`, `thumbnail_storage_path`, `taken_date`, `upload_date`, `is_featured`, `visibility`, `sort_order`)",
    "VALUES",
  ];

  const values = photos.map(([sort, title, note, fileId, date, featured, upload]) => {
    const file = resolveFile(fileId);
    const uploadPath = `/uploads/photos/${file}`;
    const storagePath = `photos/${file}`;
    return `(${sqlText(title)}, ${sqlText(note)}, ${sqlText(uploadPath)}, ${sqlText(uploadPath)}, ${sqlText(storagePath)}, ${sqlText(storagePath)}, '${date}', '${upload}', ${featured}, 'PUBLIC', ${sort})`;
  });

  lines.push(`${values.join(",\n")};`, "");
  return lines.join("\n");
};

const buildMemoryPlain = () => {
  const coverPaths = memories
    .map(([, , , , fileId]) => sqlText(`photos/${resolveFile(fileId)}`))
    .join(",\n  ");

  const lines = [
    "SET NAMES utf8mb4;",
    "USE `xiaoke_daily`;",
    "",
    "DELETE FROM `memory_entry`",
    "WHERE `cover_storage_path` IN (",
    `  ${coverPaths}`,
    ");",
    "",
    "INSERT INTO `memory_entry`",
    "(`title`, `summary`, `content`, `memory_date`, `cover_image_url`, `cover_storage_path`, `mood_tags`, `is_featured`, `visibility`, `sort_order`, `favorite_count`)",
    "VALUES",
  ];

  const values = memories.map(([sort, title, summary, content, fileId, date, tags, featured]) => {
    const file = resolveFile(fileId);
    const uploadPath = `/uploads/photos/${file}`;
    const storagePath = `photos/${file}`;
    return `(${sqlText(title)}, ${sqlText(summary)}, ${sqlText(content)}, '${date}', ${sqlText(uploadPath)}, ${sqlText(storagePath)}, ${sqlText(tags)}, ${featured}, 'PUBLIC', ${sort}, NULL)`;
  });

  lines.push(`${values.join(",\n")};`, "");
  return lines.join("\n");
};

const buildPhotoUtf8 = () => {
  const lines = [
    "SET NAMES utf8mb4;",
    "USE `xiaoke_daily`;",
    "",
    "DELETE FROM `photo`",
    "WHERE `storage_path` REGEXP '^photos/(24[0-9]|25[0-1])_';",
    "",
  ];

  for (const [sort, title, note, fileId, date, featured, upload] of photos) {
    const file = resolveFile(fileId);
    const uploadPath = `/uploads/photos/${file}`;
    const storagePath = `photos/${file}`;
    lines.push(
      "INSERT INTO `photo` (`title`,`diary_note`,`image_url`,`thumbnail_url`,`storage_path`,`thumbnail_storage_path`,`taken_date`,`upload_date`,`is_featured`,`visibility`,`sort_order`) VALUES (",
      `  ${hexExpr(title)},`,
      `  ${hexExpr(note)},`,
      `  ${hexExpr(uploadPath)},`,
      `  ${hexExpr(uploadPath)},`,
      `  ${hexExpr(storagePath)},`,
      `  ${hexExpr(storagePath)},`,
      `  '${date}',`,
      `  '${upload}',`,
      `  ${featured},`,
      "  'PUBLIC',",
      `  ${sort}`,
      ");",
      ""
    );
  }

  return lines.join("\n");
};

const buildMemoryUtf8 = () => {
  const lines = [
    "SET NAMES utf8mb4;",
    "USE `xiaoke_daily`;",
    "",
    "DELETE FROM `memory_entry`",
    "WHERE `cover_storage_path` IN (",
  ];

  const deletePaths = memories.map(([, , , , fileId]) => `  ${sqlText(`photos/${resolveFile(fileId)}`)}`);
  lines.push(deletePaths.join(",\n"));
  lines.push(");", "");

  for (const [sort, title, summary, content, fileId, date, tags, featured] of memories) {
    const file = resolveFile(fileId);
    const uploadPath = `/uploads/photos/${file}`;
    const storagePath = `photos/${file}`;
    lines.push(
      "INSERT INTO `memory_entry` (`title`,`summary`,`content`,`memory_date`,`cover_image_url`,`cover_storage_path`,`mood_tags`,`is_featured`,`visibility`,`sort_order`,`favorite_count`) VALUES (",
      `  ${hexExpr(title)},`,
      `  ${hexExpr(summary)},`,
      `  ${hexExpr(content)},`,
      `  '${date}',`,
      `  ${hexExpr(uploadPath)},`,
      `  ${hexExpr(storagePath)},`,
      `  ${hexExpr(tags)},`,
      `  ${featured},`,
      "  'PUBLIC',",
      `  ${sort},`,
      "  NULL",
      ");",
      ""
    );
  }

  return lines.join("\n");
};

fs.writeFileSync(photoOut, buildPhotoPlain(), "utf8");
fs.writeFileSync(memoryOut, buildMemoryPlain(), "utf8");
fs.writeFileSync(photoUtf8Out, buildPhotoUtf8(), "utf8");
fs.writeFileSync(memoryUtf8Out, buildMemoryUtf8(), "utf8");

console.log(photoOut);
console.log(memoryOut);
console.log(photoUtf8Out);
console.log(memoryUtf8Out);
