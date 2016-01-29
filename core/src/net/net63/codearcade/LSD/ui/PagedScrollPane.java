package net.net63.codearcade.LSD.ui;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class PagedScrollPane extends ScrollPane {

	private boolean wasPanDragFling = true;
    public boolean preventFirstAndLast = false;
	private boolean first = true;

	private Table content;

	public PagedScrollPane () {
		super(null);
		setup();
	}

	public PagedScrollPane (Skin skin) {
		super(null, skin);
		setup();
	}

	public PagedScrollPane (Skin skin, String styleName) {
		super(null, skin, styleName);
		setup();
	}

	public PagedScrollPane (Actor widget, ScrollPaneStyle style) {
		super(null, style);
		setup();
	}

	private void setup() {
		content = new Table();
		content.defaults().space(50);
		super.setWidget(content);		
	}

	public void addPage (Actor page) {
		content.add(page).expandY().fillY();
	}

	@Override
	public void act (float delta) {
		super.act(delta);

		if (first) {
			first = false;
			scrollToPage(1);
		}

		if (wasPanDragFling && !isPanning() && !isDragging() && !isFlinging()) {
			wasPanDragFling = false;
			scrollToPage();

            Array<Actor> pages = content.getChildren();
            int maxPage = pages.size - 2;
            if (preventFirstAndLast) {
                if (getScrollX() <= pages.get(1).getX() - pages.get(1).getWidth() * 0.5f) scrollToPage(1);
                if (getScrollX() >= pages.get(maxPage).getX() - pages.get(maxPage).getWidth()) scrollToPage(maxPage);
            }
		} else {
			if (isPanning() || isDragging() || isFlinging()) {
				wasPanDragFling = true;
			}
		}
	}

	@Override
	public void setWidget (Actor widget) {
		//throw new UnsupportedOperationException("Use PagedScrollPane#addPage.");
	}
	
	@Override
	public void setWidth (float width) {
		super.setWidth(width);
		if (content != null) {
			for (Cell cell : content.getCells()) {
				cell.width(width);
			}
			content.invalidate();
		}
	}

	public void setPageSpacing (float pageSpacing) {
		if (content != null) {
			content.defaults().space(pageSpacing);
			for (Cell cell : content.getCells()) {
				cell.space(pageSpacing);
			}
			content.invalidate();
		}
	}

    public int getCurrentPage() {
        float scrollX = getScrollX();

        int i = 0;
        for (Actor a : content.getChildren()) {
            if (scrollX < (a.getX() + a.getWidth() * 0.5)) break;
            i++;
        }

        return i;
    }

    public Table getContent() {
        return content;
    }

    public void scrollToPage (int n) {
        Actor page = content.getChildren().get(n);

        setScrollX(MathUtils.clamp(page.getX() - (getWidth() - page.getWidth()) / 2, 0, getMaxX()));
    }

	private void scrollToPage () {
		final float width = getWidth();
		final float scrollX = getScrollX();
		final float maxX = getMaxX();

		if (scrollX >= maxX || scrollX <= 0) return;

		Array<Actor> pages = content.getChildren();
		float pageX = 0;
		float pageWidth = 0;
		if (pages.size > 0) {
            boolean first = true;
			for (Actor a : pages) {
                if (first && preventFirstAndLast) {
                    first = false;
                    continue;
                }
                if (pages.get(pages.size - 1) == a && preventFirstAndLast) break;

				pageX = a.getX();
				pageWidth = a.getWidth();
				if (scrollX < (pageX + pageWidth * 0.5)) {
					break;
				}
			}
			setScrollX(MathUtils.clamp(pageX - (width - pageWidth) / 2, 0, maxX));
		}
	}

}