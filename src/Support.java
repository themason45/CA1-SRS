import java.util.*;
import java.util.stream.Collectors;

public class Support<T extends BaseModel> {
    /**
     * Return an object in an Array based on its pk
     *
     * @param arr The input array
     * @param pk The input pk
     * @return The object in that list that corresponds to that pk, returns null if it doesnt exist.
     */
    public T getObjectByPk(T[] arr, int pk) {
        for (T obj : arr) {
            if (obj.pk == pk) {
                return obj;
            }
        }
        return null;
    }

    /**
     * Assuming the pk stays the same, then update the value with that pk
     * @param arr The input array
     * @param newValue The new value to update the array value with
     */
    public void updateArr(T[] arr, T newValue) {
        int i = 0;
        for (T obj : arr) {
            if (obj.pk == newValue.getPk()) {
                arr[i] = newValue;
            }
            i++;
        }
    }


    /**
     * @param arr The input array
     * @param pks The input pk
     * @return A list of the objects in a list with those corresponding pks
     */
    @SuppressWarnings("unused")
    public ArrayList<T> getObjectsByPks(T[] arr, int[] pks) {
        ArrayList<T> outputArray = new ArrayList<>();
        for (int pk : pks) {
            outputArray.add(this.getObjectByPk(arr, pk));
        }
        return outputArray;
    }


    /**
     * @param input String in the format [a b c d]
     * @return Parsed input list into a Java array
     */
    public static String[] parseSublist(String input) {
        return input.replaceAll("[\\[\\]]", "").split(" ");
    }


    /**
     * @param input String in the format [a b c d]
     * @return Parsed input list into a Java array where each value is a double
     */
    public static ArrayList<Double> parseSublistAsDoubles(String input) {
        String[] stringList =  input.replaceAll("[\\[\\]]", "").split(" ");
        return (ArrayList<Double>) Arrays.stream(stringList).map(Double::parseDouble).collect(Collectors.toList());
    }


    /**
     * @param arr The input array
     * @param object The object
     * @return Whether that Object's PK exists in the Array
     */
    public Boolean uniquePk(T[] arr, T object) {
        for (T obj :
                arr) {
            if (obj.pk == object.getPk()) {
                return false;
            }
            }
        return true;
    }

}
