package ru.ssau.tk.labwork.ooplabworks.functions;

import ru.ssau.tk.labwork.ooplabworks.exceptions.InterpolationException;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removeable {

  static  private class Node {
        public Node next;
        public Node prev;
        public double x;
        public double y;
    }

    private Node head;

    protected void addNode(double x, double y) {
        if (count == 0) {
            head = new Node();
            head.x = x;
            head.y = y;
            head.next = head;
            head.prev = head;
        }
        else {
            Node newEl = new Node();
            newEl.x = x;
            newEl.y = y;

            Node temp = head;
            while (temp.next != head)
                temp = temp.next;

            temp.next = newEl;
            newEl.prev = temp;
            newEl.next = head;
            head.prev = newEl;
        }
        ++count;
    }

    public LinkedListTabulatedFunction(double[] xValues, double[] yValues) {

        checkLengthIsTheSame(xValues, yValues);
        checkSorted(xValues);

        for (int i = 0; i < xValues.length; ++i) {
            addNode(xValues[i], yValues[i]);
        }
    }

    public LinkedListTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {
        if (xFrom > xTo) {
            double temp = xFrom;
            xFrom = xTo;
            xTo = temp;
        }
        if (xFrom == xTo) {
            for (int i = 0; i < count; ++i) {
                addNode(xFrom, source.apply(xFrom));
            }
        }
        else {
            addNode(xFrom, source.apply(xFrom));
            double step = (xTo - xFrom) / (count - 1);
            double temp = step;
            for (int i = 1; i < count - 1; ++i) {
                addNode(temp, source.apply(temp));
                temp += step;
            }
            addNode(xTo, source.apply(xTo));
        }
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public double leftBound() {
        return head.x;
    }

    @Override
    public double rightBound() {
        return head.prev.x;
    }

    protected Node getNode(int index) {
        Node temp = head;
        if (index >= 0) {
            for (int i = 0; i < index; ++i) {
                temp = temp.next;
            }
        }
        else {
            index *= -1;
            for (int i = 0; i < index; ++i) {
                temp = temp.prev;
            }
        }
        return temp;
    }

    @Override
    public double getX(int index) {
        return getNode(index).x;
    }

    @Override
    public double getY(int index) {
        return getNode(index).y;
    }

    @Override
    public void setY(int index, double value) {
        getNode(index).y = value;
    }

    @Override
    public int indexOfX(double x) {
        for (int i = 0; i < count; ++i) {
            if(getNode(i).x == x)
                return i;
        }
        return -1;
    }

    @Override
    public int indexOfY(double y) {
        for (int i = 0; i < count; ++i) {
            if(getNode(i).y == y)
                return i;
        }
        return -1;
    }

    @Override
    protected int floorIndexOfX(double x) {
        if (x > getNode(count - 1).x)
            return  count - 1;
        if (x < getNode(0).x)
            return 0;
        for (int i = 0; i < count - 1; ++i) {
                if (getNode(i + 1).x > x)
                    return i;
        }
        return count - 1;
    }

    @Override
    public double apply(double x) {
        if (x < getX(0))
            return extrapolateLeft(x);
        if (x > getX(getCount() - 1))
            return extrapolateRight(x);
        int index = indexOfX(x);
        if (index != -1)
            return getY(index);
        int nodeEl = floorIndexOfX(x);
        return interpolate(x, nodeEl);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {
        if (count == 1)
            return getY(0);


        if (floorIndex < 0 || floorIndex > count - 1 || x < getX(floorIndex) || x > getX(floorIndex + 1))
            throw new InterpolationException("Не верный диапазон интерполирования");


        double leftX = getX(floorIndex);
        double leftY = getY(floorIndex);
        double rightX = getX(floorIndex + 1);
        double rightY = getY(floorIndex + 1);

        return interpolate(x, leftX, rightX, leftY, rightY);
    }

    @Override
    protected double extrapolateLeft(double x) {
        if (count == 1)
            return getY(0);
        return interpolate(x, getX(0), getX(1), getY(0), getY(1));
    }

    @Override
    protected double extrapolateRight(double x) {
        if (count == 1)
            return getY(0);
        int k = count - 1;
        return interpolate(x, getX(k - 1), getX(k), getY(k - 1), getY(k));
    }

    @Override
    public void remove(int index) {
        Node remEl = getNode(index);

        if(count == 0) {
            head.prev = null;
            head.next = null;
            head = null;
        }
        else if(head == remEl) {
            head = head.next;
            head.prev.prev.next = head;
            head.prev = head.prev.prev;
            remEl.next = null;
            remEl.prev = null;
        }
        else {
            remEl.prev.next = remEl.next;
            remEl.next.prev = remEl.prev;
            remEl.next = null;
            remEl.prev = null;
        }
        --count;
    }
    @Override
    public void insert(double x, double y) {
        // Если список пустой, просто добавляем узел
        if (count == 0) {
            addNode(x, y);
            return;
        }

        // Проверяем, существует ли уже узел с таким x
        for (int i = 0; i < count; ++i) {
            Node current = getNode(i);
            if (current.x == x) {
                // Если нашли узел с таким x, заменяем y и завершаем
                current.y = y;
                return;
            }
        }

        // Ищем место для вставки (первый узел, у которого x больше вставляемого)
        Node current = head;
        int index = 0;
        while (current.x < x && index < count) {
            current = current.next;
            index++;
        }

        // Создаем новый узел
        Node newNode = new Node();
        newNode.x = x;
        newNode.y = y;

        if (current.x < x) {
            // Вставка в конец списка (после последнего элемента)
            Node last = head.prev;
            last.next = newNode;
            newNode.prev = last;
            newNode.next = head;
            head.prev = newNode;
        } else if (current == head) {

            newNode.next = head;
            newNode.prev = head.prev;
            head.prev.next = newNode;
            head.prev = newNode;
            head = newNode;
        } else {

            newNode.next = current;
            newNode.prev = current.prev;
            current.prev.next = newNode;
            current.prev = newNode;
        }

        count++;
    }
}