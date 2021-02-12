import java.util.*;
import java.util.stream.Collectors;

public class Support<T extends BaseModel> {
    /**
     * Return an object in an Array based on its pk
     *
     * @param arr : The input array
     * @param pk : The input pk
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
     * @param arr : The input array
     * @param newValue : The new value to update the array value with
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

    public Map<Integer, T> getObjectMapByPk(T[] arr, int pk) {
        int i = 0;
        for (T obj : arr) {
            if (obj.pk == pk) {
                Map<Integer, T> map = new HashMap<>();
                map.put(i, obj);
                return map;
            }
            i++;
        }
        return null;
    }

    public ArrayList<T> getObjectsByPks(T[] arr, int[] pks) {
        ArrayList<T> outputArray = new ArrayList<>();
        for (int pk : pks) {
            outputArray.add(this.getObjectByPk(arr, pk));
        }
        return outputArray;
    }

    public static String[] parseSublist(String input) {
        return input.replaceAll("[\\[\\]]", "").split(" ");
    }

    public static ArrayList<Double> parseSublistAsDoubles(String input) {
        String[] slist =  input.replaceAll("[\\[\\]]", "").split(" ");
        return (ArrayList<Double>) Arrays.stream(slist).map(Double::parseDouble).collect(Collectors.toList());
    }
}
