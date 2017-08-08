package gce.swing;

/**<p>
* =============================================================================
* <p>文件名称：BasePanel.java
* <p>文件标识：见配置管理计划书
* <p>摘    要：抽象基础面板
* @version 2.0
* @author 
* 
=============================================================================*/

import java.awt.*;
import javax.swing.*;

public abstract class BasePanel extends JPanel {
   /*当前面板的容器*/
   protected Container container;
   /*网格布局器*/
   protected GridBagLayout layout;
   /*网格布局器的约束器*/
   protected GridBagConstraints constraints;

   public BasePanel() {
       container = this;
       layout = new GridBagLayout();
       constraints = new GridBagConstraints();
       container.setLayout(layout);
       constraints.fill = constraints.BOTH; //在水平方向和垂直方向上同时调整组件大小
       initAllComponents();
       layoutAllComponents();
   }

   /**<p>
    * =========================================================================
    * <p>初始化所有GUI组件.<p>
    =========================================================================*/
   protected abstract void initAllComponents();

   /**@todo 继承之子类完成所有组件的初始化工作 */

   /**<p>
    * =========================================================================
    * <p>布局所有已经初始化了的GUI组件
    *<p><blockquote><pre>
    *   布局器，约束器必须已经初始化好
    *   然后再调用布局子函数进行网格布局
    *</pre></blockquote></p>
    =========================================================================*/
   protected abstract void layoutAllComponents();

   /**@todo 继承之子类完成组件具体的布局 */

   /**<p>
    * =========================================================================
    * <p> 工具函数：用于获取当前屏幕的分辨率
    * @return Dimension 屏幕大小<p>
    =========================================================================*/
   public Dimension getScreenSize() {
       return Toolkit.getDefaultToolkit().getScreenSize();
   }

   /**<p>
    * ==========================================================================
    * <p> 设置将本程序框架大小并将其定位于屏幕正中心
    * @param container Container 要设置的容器
    * @param width 本框架的宽度
    * @param height 本框架的高度<p>
    ==========================================================================*/
   protected void putToCenter(Component container, int width, int height) {
       container.setSize(width, height); //设置大小
       container.setLocation
               ( //定位框架位置
                       (int) (Toolkit.getDefaultToolkit().getScreenSize().
                              getWidth() - width) / 2,
                       (int) (Toolkit.getDefaultToolkit().getScreenSize().
                              getHeight() - height) / 2
               );
   }

   /**<p>
    * =========================================================================
    * <P> 获取整个桌面的大小
    * @return Rectangle 桌面大小.<p>
    ==========================================================================*/
   public java.awt.Rectangle getDesktopBounds() {
       GraphicsEnvironment env =
               GraphicsEnvironment.getLocalGraphicsEnvironment();
       return env.getMaximumWindowBounds();
   }

   /**<p>
    * =========================================================================
    * <p> 设置将本程序框架大小并将其定位于屏幕(X,Y)坐标处宽和高为 width, height
    * @param container Container 要设置的容器
    * @param width int  宽度
    * @param height int 高度
    * @param startX int 放置位置的X坐标
    * @param startY int 放置位置的Y坐标<p>
    =========================================================================*/
   protected void putToPlace(Container container, int width, int height,
                             int startX, int startY
           ) {
       container.setLocation(startX, startY);
       container.setSize(width, height); //设置大小
   }

   /**<p>
    * =========================================================================
    * <p> 功能强大的工具函数:布局器
    * <p> 具体功能:
    * <p> 将 componentToAdd 组件安装到 container 容器中的
    * <p> 第 row 行， 第 column 列，并在 container 容器中
    * <p> 占据 height 行，width 列, 在X,Y方向分别可伸展x和y长
    * <p> 备注：要能如此挑剔的地布局,container 必须预先设定为网格布局器:GridBagLayout,
    * <p>       然后用 GridBagContstraints 协调布局
    * <p>原函数来自"Java程序设计教程"第五版 P530页,函数有被我加强功能
    * @param container   Container 要布局的容器
    * @param layout      GridBagLayout 网格布局器
    * @param constraints GridBagConstraints 约束器
    * @param componentToAdd Component 要添加的目标组件
    * @param row         目标组件要添加的行
    * @param column      目标组件要添加的列
    * @param height      目标组件占据的高度
    * @param width       目标组件占据的宽度
    * @param x           目标组件在水平方向的拉伸能力
    * @param y           目标组件在垂直方向的拉伸能力<p>
    =========================================================================*/
   protected void addComponent(
           Container container, GridBagLayout layout,
           GridBagConstraints constraints,
           Component componentToAdd, int row, int column, int height,
           int width, int x, int y) {
       constraints.gridx = column; //组件所在列
       constraints.gridy = row; //组件所在行

       constraints.gridwidth = width; //组件宽度
       constraints.gridheight = height; //组件高度

       constraints.weightx = x; //组件在水平方向的拉伸能力
       constraints.weighty = y; //组件在垂直方向的拉伸能力

       layout.setConstraints(componentToAdd, constraints); //设置目标组件的约束
       container.add(componentToAdd); //在容器中添加目标组件
   }

   /**<p>
    * =========================================================================
    * <p> 功能强大的工具函数:布局器
    * <p> 将指定组件安装到此 Panel 内
    * @param componentToAdd Component 要添加的目标组件
    * @param row         目标组件要添加的行
    * @param column      目标组件要添加的列
    * @param height      目标组件占据的高度
    * @param width       目标组件占据的宽度
    * @param x           目标组件在水平方向的拉伸能力
    * @param y           目标组件在垂直方向的拉伸能力<p>
    =========================================================================*/
   protected void addComponent(Component componentToAdd,
                               int row, int column, int height,
                               int width, int xAblility, int yAblity) {
       addComponent(container, layout, constraints, componentToAdd,
                    row, column, height, width, xAblility, yAblity);
   }
}
