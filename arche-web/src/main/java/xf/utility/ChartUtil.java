package xf.utility;

import com.google.gson.internal.Pair;
import com.ibm.icu.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


/**
 * @classname: ChartUtil
 * @description: 图形Util类
 */
public class ChartUtil {

    static Log log = LogFactory.getLog(ChartUtil.class);

    private static int pictureWidth = 545; // 图片宽度
    private static int pictureHeight = 300; // 图片高度
    private static Color[] colors = {new Color(255, 165, 165), new Color(170, 140, 226), new Color(246, 116, 196), new Color(255, 195, 52),
            new Color(252, 147, 77), new Color(117, 201, 106), new Color(50, 190, 235)};

//	public static void main(String[] args) {
//		String[] dataTitle = { "3C电器", "服装饰品", "母婴用品", "食品饮料", "厨卫家居", "美容护理", "礼品卡虚拟服务" };
//		Double[] data = { 33798.33, 22395.63, 27330.42, 8281.4, 8514.1, 396.4, 224.8};
//		String filePath = "D:\\workspace\\SLR-member-vip\\src\\main\\webapp\\statics\\bill\\images\\xiaoren\\";
////		String imageFile = filePath + "cost.jpg";
//		ChartUtil.drawSpendBill(dataTitle, data, filePath, "cost.jpg", new Pair<String, String>("花钱小王子", "99%"), "p10.jpg");

    // 赚了多少
//		String imageFile = "D:/workspace/SLR-member-vip/src/main/webapp/WEB-INF/template/earn.jpg";
//		int[] x = {128, 387, 612, 858, 40, 280, 520, 770};
//		int[] y = {185, 185, 185, 185, 255, 255, 255, 255};
//		String[] txt = {"1", "363", "46227.0", "30999", "别再让抵用券君，苦苦等待，<br>赶快购物吧！", "可以兑换商品、抵用券，更有机<br>会抽得500万大奖!", "成为1号店会员最高尊享10余项<br>VIP权益哦！", "别哇塞！您本月获得了309枚<br>勋章，又可以享受优惠啦！"};
//		ChartUtil.drawTextOnImage(imageFile, x, y, txt, true);
//
//		 省了多少
//		String imageFile = "D:/workspace/SLR-member-vip/src/main/webapp/WEB-INF/template/save.jpg";
//		int[] x = {246, 102, 180, 336, 380, 102, 152, 336, 380, 750, 600, 750, 600};
//		int[] y = {120, 195, 218, 195, 218, 282, 302, 282, 302, 120, 150, 260, 290};
//		String[] txt = {
//				"¥80396.29"
//				, "352", "¥77609.29"
//				, "1", "¥30"
//				, "16", "¥197"
//				, "167", "¥2560"
//				, "292.7公斤", "呀，你买的商品都不需要快递搬运呢，真能干！"
//				, "186小时", "您本月在我店下单 186次，平均每次 10 分钟，<br>足足节约了186个小时，都可以粗去批次情操啦！"
//		};
//		ChartUtil.drawTextOnImage(imageFile, x, y, txt, false);
//	}

    /**
     * @title: drawSpendBill
     * @description: 【月度账单】画饼状图
     * @author wanjinwei
     * @date 2014-7-31 上午11:02:23
     * @param dataTitle 标题
     * @param data 数据
     * @param filePath 服务器端存储路径
     * @param imgTemplet 模板文件名
     * @param txtInfo 排名信息
     * @param rankImg 排名图片名
     * @return BufferedImage
     */
    public static BufferedImage drawSpendBill(String[] dataTitle, Double[] data, String filePath, String imgTemplet, Pair<String, String> txtInfo, String rankImg) {

        int topHeight = 80;
        double percent = pictureWidth / 1000.0; // 绘图百分比
        BufferedImage image = null;
        try {
            if (StringUtils.isBlank(filePath)) {
                image = new BufferedImage(pictureWidth + 100, pictureHeight + 100, BufferedImage.TYPE_INT_RGB);
            }
            else {
                image = ImageIO.read(new FileInputStream(filePath + imgTemplet));
            }
            // 创建Java2D对象，Java2D即对二维图表的支持
            Graphics2D g2d = image.createGraphics();
            // 消除锯齿
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // 绘制图片背景
            g2d.setPaint(new Color(255, 120, 120)); // 设置颜色
            // 填充区域
            g2d.fillRect(0, topHeight, pictureWidth, pictureHeight);

            // 绘制绘图区
            g2d.setPaint(Color.WHITE);
            int a = (int) (30 * percent);
            int b = (int) (50 * percent) + topHeight;
            // 绘制饼状图
            double dataSum = 0;
            for (int i = 0; i < data.length; i++) {
                dataSum = dataSum + data[i];
            }
            g2d.setFont(new Font("MICROSOFT YAHEI", Font.PLAIN, 11));
            double startAngle = 90; // 开始绘制扇形的角度
            double arcAngle = 0; // 绘制扇形的角度
            int xCenter = pictureWidth / 2; // 饼状图圆心的x轴坐标
//			int yCenter = topHeight + (pictureWidth / 2); // 饼状图圆心的y轴坐标
            int x = 0;
            int y = 0;
//			int tempy = 0;
            String dataDescribe = "";
            double totalAmount = 0;
            // 方向：1逆时针画图、-1顺时针画图
            int direction = -1;
            for (int i = 0; i < data.length; i++) {
                g2d.setColor(colors[i]);
                startAngle = startAngle + arcAngle * direction;
                arcAngle = (data[i] * 360 / dataSum);
                // 绘制饼状图
                g2d.fillArc((int) (pictureWidth - (int) (360 * percent)) / 2, (int) (pictureHeight - (int) (360 * percent)) / 2 + (b - a) / 2, (int) (360 * percent),
                        (int) (360 * percent), (int) startAngle, ((int) arcAngle) * direction);
                // 绘制定义文字
                // 定义一个新饼图，与显示的是同心圆，但不显示，绘制的每个扇形角度为显示的一半
                BigDecimal percents = BigDecimal.valueOf(380 * percent);
                BigDecimal two = BigDecimal.valueOf(2);
                Arc2D.Double arc2d = new Arc2D.Double(
                        BigDecimal.valueOf(pictureWidth).subtract(percents).divide(two).doubleValue(),
                        BigDecimal.valueOf(pictureHeight).subtract(percents).divide(two).add(BigDecimal.valueOf((b - a)).divide(two)).doubleValue(),
                        percents.doubleValue(),
                        percents.doubleValue(),
                        BigDecimal.valueOf(startAngle).doubleValue(),
                        BigDecimal.valueOf(arcAngle).multiply(BigDecimal.valueOf(direction)).divide(two).doubleValue(),
//						(int) (pictureWidth - (int) (380 * percent)) / 2,
//						(int) (pictureHeight - (int) (380 * percent)) / 2 + (b - a) / 2,
//						(int) (380 * percent),
//						(int) (380 * percent),
//						(int) startAngle,
//						arcAngle * direction / 2,
                        Arc2D.PIE);
                // 演示确定定义文字输出位置的圆弧
                // g2d.draw(arc2d);
                // 获取新饼图的终点坐标
                Point2D.Double endPoint = (Point2D.Double) arc2d.getEndPoint();
                x = (int) endPoint.getX();
                y = (int) endPoint.getY();
                dataDescribe = "￥" + data[i];
                totalAmount += data[i];
                // 如果终点在圆心的左侧，则再向左移动定义文字的长度
                if (x < xCenter) {
//					g2d.drawLine(x, y, x - 15, y);
//					x = x - (dataTitle[i].length() * 30 + dataDescribe.length() * 6);
                    x = x - (dataTitle[i].length() + dataDescribe.length()) * 10 - 20;
                }
//				else {
//					g2d.drawLine(x, y, x + 20, y);
//					x = x + 20;
//				}
//				System.out.print("y = " + y +", ");
//				if (tempy > 0 && y < tempy + 20) {
//					y = tempy + 20;
//				}
//				tempy = y;
//				System.out.println("y = " + y);
                // 输出定义文字
                g2d.setFont(new Font("MICROSOFT YAHEI", Font.PLAIN, 14));
                g2d.setColor(new Color(238, 238, 238));
//				if (x < xCenter) {
//					if (y < yCenter) {
//						g2d.drawString(dataTitle[i] + " " + dataDescribe, x - 10, y + 5 - 10);
//					}
//					else {
//						g2d.drawString(dataTitle[i] + " " + dataDescribe, x - 10, y + 5 + 10);
//					}
//				}
//				else {
//					if (y < yCenter) {
//						g2d.drawString(dataTitle[i] + " " + dataDescribe, x + 10, y + 5 - 10);
//					}
//					else {
//						g2d.drawString(dataTitle[i] + " " + dataDescribe, x + 10, y + 5 + 10);
//					}
//				}
                g2d.drawString(dataTitle[i] + " " + dataDescribe, x, y + 5);
            }
            // 绘制圆心外阴影
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);
            g2d.setComposite(ac);
            g2d.setColor(new Color(204, 132, 132));
            g2d.fillArc((int) (pictureWidth - (int) (245 * percent)) / 2, (int) (pictureHeight - (int) (245 * percent)) / 2 + (b - a) / 2, (int) (245 * percent),
                    (int) (245 * percent), 0, 360);

            // 绘制圆心
            ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
            g2d.setComposite(ac);
            g2d.setColor(Color.WHITE);
            g2d.fillArc((int) (pictureWidth - (int) (220 * percent)) / 2, (int) (pictureHeight - (int) (220 * percent)) / 2 + (b - a) / 2, (int) (220 * percent),
                    (int) (220 * percent), 0, 360);

            // 绘制圆心上文字
            g2d.setFont(new Font("MICROSOFT YAHEI", Font.PLAIN, 20));
            g2d.setColor(new Color(153, 153, 153));
            g2d.drawString("花费", pictureWidth / 2 - 22, (pictureHeight + topHeight) / 2);
            g2d.setColor(Color.RED);
            g2d.drawString("￥" + String.format("%.2f", totalAmount), pictureWidth / 2 - 50, (pictureHeight  + topHeight)/ 2 + 20);

            // 右侧排名方案
            g2d.setFont(new Font("MICROSOFT YAHEI", Font.PLAIN, 14));
            g2d.setColor(new Color(229,87,87));
            g2d.drawString(txtInfo.first, 744, 113);
            g2d.drawString(txtInfo.second, 776, 138);

            // 右侧排名
            if (StringUtils.isNotBlank(rankImg)) {
                BufferedImage pm = ImageIO.read(new FileInputStream(filePath + rankImg));
                g2d.drawImage(pm, null, 549, 180);
            }

            // 部署图形
            g2d.dispose();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 利用ImageIO类的write方法对图像进行编码，生成png格式的图象
//		try {
//			ImageIO.write(image, "png", new File(filePath + "cost-20140731.png"));
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
        return image;
    }

    /**
     * @title: drawTextOnImage
     * @description: 在图片上添加文字
     * @author wanjinwei
     * @date 2014-7-14 上午11:29:34
     * @param imageFile
     * @param x x轴坐标
     * @param y y轴坐标
     * @param txt 要添加的文字
     * @param showDigitCenter 是否要对数字进行居中
     * @return BufferedImage
     */
    public static BufferedImage drawTextOnImage(String imageFile, int[] x, int[] y, String[] txt, boolean showDigitCenter) {

        // 检查参数
        if (x == null || y == null || txt == null || x.length != x.length || y.length != txt.length) {
            log.error("drawTextOnImage error: array is null or array length not equals others");
            return null;
        }

        BufferedImage bufferedImage = null;
        try {
            if (StringUtils.isBlank(imageFile)) {
                bufferedImage = new BufferedImage(pictureWidth + 100, pictureHeight + 100, BufferedImage.TYPE_INT_RGB);
            }
            else {
                bufferedImage = ImageIO.read(new FileInputStream(imageFile));
            }
            int w = bufferedImage.getWidth();
            int h = bufferedImage.getHeight();

            Graphics2D g = bufferedImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawImage(bufferedImage, 0, 0, w, h, null);
            g.setFont(new Font("MICROSOFT YAHEI", Font.PLAIN, 14));
            for (int i = 0; i < txt.length; i++) {
                if (showDigitCenter) {
                    x[i] = moveToCenter(txt[i], x[i]);
                }
                String[] lines = txt[i].split("<br>");
                for (int j = 0; j < lines.length; j++) {
                    g.drawString(lines[j], x[i], y[i] + j*20);
                }
            }
            g.dispose();

//			// 利用ImageIO类的write方法对图像进行编码，生成png格式的图象
//			try {
//				ImageIO.write(bufferedImage, "png", new File("D:/workspace/SLR-member-vip/src/main/webapp/WEB-INF/template/java-earn.png"));
//			} catch (IOException ex) {
//			}

        } catch (FileNotFoundException e) {
            log.error("drawTextOnImage FileNotFoundException: ", e);
        } catch (IOException e) {
            log.error("drawTextOnImage IOException: ", e);
        }
        return bufferedImage;
    }

    /**
     * @title: moveToCenter
     * @description: 把数据按所给的原点居中对齐
     * @author wanjinwei
     * @date 2014-8-4 下午04:08:34
     * @param str
     * @param x
     * @return int
     */
    private static int moveToCenter(String str, int x) {
        try {
            Double d = Double.parseDouble(str);
            x = x - d.toString().length() * 5 / 2;
            return x;
        } catch (Exception e) {
            return x;
        }
    }
}


/* Action中的示例
public void shareSave() {
    Long endUserId = SsoClientCacheUtil.getCurrentLoginUserId();
    if (endUserId == null) {
        return;
    }

    index();
    if (billMthly == null) {
        return;
    }

    String filePath = ServletActionContext.getServletContext().getRealPath("/")
            + "WEB-INF/template/";
    int[] x = { 246, 102, 180, 336, 380, 102, 152, 336, 380, 750, 600, 750, 600 };
    int[] y = { 120, 195, 218, 195, 218, 282, 302, 282, 302, 120, 150, 260, 290 };
    String[] txt = {
            "¥" + billMthly.getSaveAmt().toString(),
            billMthly.getPmNum().toString(),
            "¥" + billMthly.getSaveAmtPm().toString(),
            billMthly.getCoupnOrdrNum().toString(),
            "¥" + billMthly.getSaveAmtCoupn().toString(),
            billMthly.getPromOrdrNum().toString(),
            "¥" + billMthly.getPromAmt().toString(),
            billMthly.getFreeDlvrOrdrNum().toString(),
            "¥" + billMthly.getFreeDlvrAmt().toString(),
            billMthly.getPmWght().toString() + "公斤",
            billMthly.getPmWght() > 0 ? "哇塞！您本月购买的商品加起来有 " + billMthly.getPmWght()
                    + "公斤，<br>快递师傅全帮您搬到家啦！" : "呀，你买的商品都不需要快递搬运呢，真能干！",
            billMthly.getParntOrdrNum().toString() + "小时",
            "您本月在我店下单 " + billMthly.getParntOrdrNum() + "次，平均每次 10 分钟，<br>足足节约了"
                    + billMthly.getParntOrdrNum() + "个小时，都可以粗去批次情操啦！" };

    BufferedImage image = ChartUtil.drawTextOnImage(filePath + "save.jpg", x, y, txt, false);
    ServletOutputStream sos = null;
    // 图片文件名
    String picName = endUserId + "_" + System.currentTimeMillis() + ".png";
    try {
        sos = ServletActionContext.getResponse().getOutputStream();
        ImageIO.write(image, "png", new File(filePath + picName));
        /**
             * flush清空缓冲区数据，就是说你用读写流的时候，其实数据是先被读到了内存中，然后用数据写到文件中，
             * 当你数据读完的时候不代表你的数据已经写完了，因为还有一部分有可能会留在内存这个缓冲区中。
             * 这时候如果你调用了 close()方法关闭了读写流，那么这部分数据就会丢失，
             * 所以应该在关闭读写流之前先flush()，先清空数据。/
        sos.flush();
    } catch (IOException ex) {
        log.error("create img error: ", ex);
    }

    try {
        // 指定输出内容类型和编码
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        // 获取输出流，然后使用
        PrintWriter out = new PrintWriter(sos);
        File picFile = new File(filePath + picName);
        String picUrl = FileUploadUtil.uploadFile(endUserId, picFile, picName);

        if (StringUtils.isNotBlank(picUrl)) {
            generateResult(CODE_SUCCESS, null, picUrl);
        } else {
            generateResult(CODE_FAILURE, MSG_FAILURE, null);
        }

        out.print(gson.toJson(result));
        out.flush();
        out.close();
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (sos != null) {
            try {
                sos.close();
            } catch (IOException e) {
                log.error(e);
            }
        }
    }
}*/