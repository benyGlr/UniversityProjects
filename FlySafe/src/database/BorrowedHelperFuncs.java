package database;
import java.lang.reflect.Array;

/**
 * this class comes from the text book
 * @author Daniel Brunton, Ben Giller, Matt Kaczmarek, Jiacheng Yang, Ray Winardi
 *
 */
public class BorrowedHelperFuncs {
	static <T> T[] joinArrayGeneric(T[]... arrays) {
        int length = 0;
        for (T[] array : arrays) {
            length += array.length;
        }

        //T[] result = new T[length];
        final T[] result = (T[]) Array.newInstance(arrays[0].getClass().getComponentType(), length);

        int offset = 0;
        for (T[] array : arrays) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }

        return result;
    }

}
