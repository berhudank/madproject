package mad.focuson.views.adapters;

import static org.junit.Assert.*;
import android.content.Context;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

public class ThemeAdapterTest {

    private ThemeAdapter themeAdapter;
    private ArrayList<Integer> mockThemeImages;

    @Mock
    private Context mockContext;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockThemeImages = new ArrayList<>();
        mockThemeImages.add(android.R.drawable.ic_menu_camera);
        mockThemeImages.add(android.R.drawable.ic_menu_compass);
        mockThemeImages.add(android.R.drawable.ic_menu_gallery);
        themeAdapter = new ThemeAdapter(mockContext, mockThemeImages);
    }

    @Test
    public void testGetCount() {
        assertEquals("Adapter should return the correct item count", 3, themeAdapter.getCount());
    }

    @Test
    public void testGetItem() {
        assertEquals("Adapter should return the correct item for position 0", android.R.drawable.ic_menu_camera, themeAdapter.getItem(0));
        assertEquals("Adapter should return the correct item for position 1", android.R.drawable.ic_menu_compass, themeAdapter.getItem(1));
        assertEquals("Adapter should return the correct item for position 2", android.R.drawable.ic_menu_gallery, themeAdapter.getItem(2));
    }

    @Test
    public void testGetItemId() {
        assertEquals("Item ID should match position", 0, themeAdapter.getItemId(0));
        assertEquals("Item ID should match position", 1, themeAdapter.getItemId(1));
        assertEquals("Item ID should match position", 2, themeAdapter.getItemId(2));
    }

}
