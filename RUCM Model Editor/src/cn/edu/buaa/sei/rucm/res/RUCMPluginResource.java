package cn.edu.buaa.sei.rucm.res;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import cn.edu.buaa.sei.rucm.RUCMPlugin;

import co.gongzh.snail.Image;
import co.gongzh.snail.Image.ImageSourceLoader;
import co.gongzh.snail.ResizableImage;
import co.gongzh.snail.text.TextAntialiasing;

public final class RUCMPluginResource {
	
	public static final Stroke BASIC_STROKE = new BasicStroke();
	public static final Stroke DASH_STROKE = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, new float[] { 3.0f, 4.0f }, 0.0f);
	public static final Stroke THICK_STROKE = new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	public static final Stroke THICK_DASH_STROKE = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 20.0f, new float[] {5.0f, 8.0f}, 0.0f);
	
	public static final Color SELECTION_BORDER_COLOR = new Color(0x00bff3);
	public static final Color TEXT_BOX_TEXT_COLOR = Color.BLACK;
	public static final Color ACTOR_LABEL_TEXT_COLOR = new Color(0x153250);
	public static final Color USECASE_STROKE_COLOR = new Color(0xd0811b);
	public static final Color USECASE_STROKE_COLOR_H = new Color(0xe19d1a);
	public static final Color USECASE_BG_COLOR1 = new Color(0xfffcef);
	public static final Color USECASE_BG_COLOR2 = new Color(0xffeea1);
	public static final Color USECASE_BG_COLOR1_H = new Color(0xfffefc);
	public static final Color USECASE_BG_COLOR2_H = new Color(0xfff7d3);
	public static final Color USECASE_LABEL_TEXT_COLOR = new Color(0x452c00);
	public static final Color ARROW_STROKE_COLOR = new Color(0x6f6f6f);
	public static final Color ARROW_LABEL_COLOR = new Color(0x787878);
	public static final Color SEPERATOR_LINE_COLOR = new Color(0x6c6c6c);
	public static final Color MENU_BG_COLOR = new Color(0x4a4a4a);
	public static final Color MENU_TEXT_COLOR = new Color(0xdcdcdc);
	public static final Color PACKAGE_BG_COLOR = new Color(240, 240, 240);
	public static final Color PACKAGE_BORDER_COLOR = new Color(0xa9a9a9);
	public static final Color PACKAGE_TEXT_COLOR = new Color(0x7e7e7e);
	
	public static final Font FONT_DIALOG = new Font(Font.DIALOG, Font.PLAIN, 12);
	public static final Font FONT_DIALOG_BOLD = new Font(Font.DIALOG, Font.BOLD, 12);
	public static final Font FONT_DIALOG_TITLE = new Font(Font.DIALOG, Font.PLAIN, 20);
	public static final Font FONT_DIALOG_SUBTITLE = new Font(Font.DIALOG, Font.PLAIN, 16);
	
	public static interface SpecFontChangeListener {
		public void fontChanged(Font plainFont, Font boldFont);
	}
	
	public static interface SpecTextAAModeChangeListener {
		public void textAAModeChanged(TextAntialiasing mode);
	}
	
	private static final List<SpecFontChangeListener> FONT_LISTENERS = new Vector<RUCMPluginResource.SpecFontChangeListener>();
	private static final List<SpecTextAAModeChangeListener> TEXTAA_LISTENERS = new Vector<RUCMPluginResource.SpecTextAAModeChangeListener>();
	
	private RUCMPluginResource() {
	}
	
	private static boolean loaded = false;
	private static GraphicsConfiguration gc;
	private static final Map<String, Image> imageMap = new HashMap<String, Image>();
	private static final Map<String, Font> fontMap = new HashMap<String, Font>();
	private static final Map<String, TextAntialiasing> textaaMap = new HashMap<String, TextAntialiasing>();
	
	public static void initResource(GraphicsConfiguration gc) {
		if (!loaded) {
			loaded = true;
			RUCMPluginResource.gc = gc;
			loadImage(gc, "scrollbar.png", 3, 4, 4, 5);
			loadImage(gc, "sectionbar.png", 3, 3, 5, 27);
			loadImage(gc, "dropshadow_h.png", 6, 0, 7, 3);
			loadImage(gc, "dropshadow_v.png", 0, 5, 3, 6);
			loadImage(gc, "button.png", 2, 1, 4, 43);
			loadImage(gc, "textbox.png", 5, 9, 6, 19);
			loadImage(gc, "menu.png", 9, 9, 11, 11);
			loadImage(gc, "brackets.png", 8, 4, 10, 5);
			loadImage(gc, "dropdownlist.png", 7, 5, 8, 7);
			
			textaaMap.put("on", TextAntialiasing.ON);
			textaaMap.put("off", TextAntialiasing.OFF);
			textaaMap.put("hbgr", TextAntialiasing.LCD_HBGR);
			textaaMap.put("hrgb", TextAntialiasing.LCD_HRGB);
			textaaMap.put("vbgr", TextAntialiasing.LCD_VBGR);
			textaaMap.put("vrgb", TextAntialiasing.LCD_VRGB);
			
			RUCMPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent event) {
					if (event.getProperty().equals(RUCMPlugin.PK_SPEC_EDITOR_FONT_NAME) ||
						event.getProperty().equals(RUCMPlugin.PK_SPEC_EDITOR_FONT_SIZE)) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								Font plainFont = RUCMPluginResource.getSpecFont(false);
								Font boldFont = RUCMPluginResource.getSpecFont(true);
								for (SpecFontChangeListener listener : FONT_LISTENERS) {
									listener.fontChanged(plainFont, boldFont);
								}
							}
						});
					} else if (event.getProperty().equals(RUCMPlugin.PK_SPEC_EDITOR_TEXTAA)) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								TextAntialiasing aa = RUCMPluginResource.getSpecTextAntialiasing();
								for (SpecTextAAModeChangeListener listener : TEXTAA_LISTENERS) {
									listener.textAAModeChanged(aa);
								}
							}
						});
					}
				}
			});
		}
	}
	
	public static void addSpecFontChangeListener(SpecFontChangeListener listener) {
		FONT_LISTENERS.add(listener);
	}
	
	public static void removeSpecFontChangeListener(SpecFontChangeListener listener) {
		FONT_LISTENERS.remove(listener);
	}
	
	public static void addSpecTextAAModeChangeListener(SpecTextAAModeChangeListener listener) {
		TEXTAA_LISTENERS.add(listener);
	}
	
	public static void removeSpecTextAAModeChangeListener(SpecTextAAModeChangeListener listener) {
		TEXTAA_LISTENERS.remove(listener);
	}
	
	public static Font getSpecFont(boolean bold) {
		IPreferenceStore store = RUCMPlugin.getDefault().getPreferenceStore();
		String name = store.getString(RUCMPlugin.PK_SPEC_EDITOR_FONT_NAME);
		int size = store.getInt(RUCMPlugin.PK_SPEC_EDITOR_FONT_SIZE);
		if (name.isEmpty()) name = Font.DIALOG_INPUT;
		if (size == 0) size = 12;
		String encode = String.format("%s-%s-%d", name, bold ? "BOLD" : "PLAIN", size);
		if (fontMap.containsKey(encode)) {
			return fontMap.get(encode);
		} else {
			Font font = Font.decode(encode);
			fontMap.put(encode, font);
			return font;
		}
	}
	
	public static TextAntialiasing getSpecTextAntialiasing() {
		IPreferenceStore store = RUCMPlugin.getDefault().getPreferenceStore();
		String mode = store.getString(RUCMPlugin.PK_SPEC_EDITOR_TEXTAA);
		if (mode != null && textaaMap.containsKey(mode)) return textaaMap.get(mode);
		else return TextAntialiasing.ON;
	}
	
	private static void loadImage(GraphicsConfiguration gc, String name) {
		imageMap.put(name, new Image(gc, new BundleImageLoader(name)));
	}
	
	private static void loadImage(GraphicsConfiguration gc, String name, int x1, int y1, int x2, int y2) {
		imageMap.put(name, new ResizableImage(gc, new BundleImageLoader(name), x1, y1, x2, y2));
	}
	
	public static Image getImage(String name) {
		if (!imageMap.containsKey(name)) {
			loadImage(gc, name);
		}
		return imageMap.get(name);
	}
	
	public static GraphicsConfiguration getGraphicsConfiguration() {
		return gc;
	}
	
	private static class BundleImageLoader implements ImageSourceLoader {

		private final String name;
		
		BundleImageLoader(String name) {
			this.name = name;
		}
		
		@Override
		public BufferedImage loadImage() {
			try {
				return ImageIO.read(RUCMPluginResource.class.getResourceAsStream(name));
			} catch (IOException ex) {
				return null;
			}
		}

		@Override
		public void unloadImage(BufferedImage image) {
			image.flush();
		}
		
	}

}
