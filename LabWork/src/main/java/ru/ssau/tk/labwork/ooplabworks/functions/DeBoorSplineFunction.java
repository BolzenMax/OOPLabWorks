package ru.ssau.tk.labwork.ooplabworks.functions;
import java.util.Arrays;

public class DeBoorSplineFunction {

    /**
     * Алгоритм Де Бура для вычисления точки на B-сплайне
     *
     * @param degree степень B-сплайна
     * @param knots вектор узлов
     * @param controlPoints контрольные точки
     * @param t параметр для вычисления точки
     * @return точка на B-сплайне при параметре t
     */
    public static double[] deBoor(int degree, double[] knots, double[][] controlPoints, double t) {
        // Проверка корректности входных данных
        if (knots == null || controlPoints == null) {
            throw new IllegalArgumentException("Knots and control points cannot be null");
        }

        if (degree < 0) {
            throw new IllegalArgumentException("Degree must be non-negative");
        }

        if (knots.length != controlPoints.length + degree + 1) {
            throw new IllegalArgumentException("Invalid number of knots. Expected: " +
                    (controlPoints.length + degree + 1) + ", got: " + knots.length);
        }

        // Находим интервал, в котором находится t
        int k = findKnotSpan(degree, knots, t);

        // Инициализируем массив для промежуточных точек
        double[][] d = new double[degree + 1][];
        for (int i = 0; i <= degree; i++) {
            d[i] = Arrays.copyOf(controlPoints[k - degree + i], controlPoints[0].length);
        }

        // Выполняем алгоритм Де Бура
        for (int r = 1; r <= degree; r++) {
            for (int j = degree; j >= r; j--) {
                double alpha = (t - knots[j + k - degree]) /
                        (knots[j + 1 + k - r] - knots[j + k - degree]);

                // Линейная интерполяция для каждой координаты
                for (int dim = 0; dim < d[j].length; dim++) {
                    d[j][dim] = (1 - alpha) * d[j - 1][dim] + alpha * d[j][dim];
                }
            }
        }

        return d[degree];
    }

    /**
     * Находит интервал узлового вектора, содержащий параметр t
     */
    private static int findKnotSpan(int degree, double[] knots, double t) {
        int n = knots.length - degree - 2;

        // Особый случай для последнего узла
        if (t >= knots[n + 1]) {
            return n;
        }

        // Бинарный поиск
        int low = degree;
        int high = n + 1;
        int mid = (low + high) / 2;

        while (t < knots[mid] || t >= knots[mid + 1]) {
            if (t < knots[mid]) {
                high = mid;
            } else {
                low = mid;
            }
            mid = (low + high) / 2;
        }

        return mid;
    }

    /**
     * Упрощенный метод для кубических B-сплайнов (степень 3)
     */
    public static double[] deBoorCubic(double[] knots, double[][] controlPoints, double t) {
        return deBoor(3, knots, controlPoints, t);
    }
}