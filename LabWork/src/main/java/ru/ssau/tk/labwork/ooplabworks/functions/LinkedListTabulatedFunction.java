package ru.ssau.tk.labwork.ooplabworks.functions;

/* АХТУНГ! Это самая предварительная версия реализации данного класса!
Надо дождаться реализации других используемых тут классов!
Например, AbstractTabulatedFunction!
Также внутри этого класса надо дописывать согласно ТЗ некоторые методы
Возможно даже придётся переписать этот код полностью (надеюсь не потребуется)...
Ну а также надо будет покрыть этот и не только классы тестами! */

/* public class LinkedListTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removeable {

    private class Node {
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
        else if (xFrom == xTo) {
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

    // Методы floorIndexOfX и floorNodeOfX, хз-хз
    @Override
    protected int floorIndexOfX(double x) {
        if (x > getNode(count - 1).x)
            return  count - 1;
        if (x < getNode(0).x)
            return 0;
        for (int i = 0; i < count - 1; ++i) {
            if (getNode(i).x <= x) {
                if (getNode(i + 1).x > x)
                    return i;
            }
        }
        return count - 1;
    }

    protected Node floorNodeOfX(double x) {
        if (x > getNode(count - 1).x)
            return getNode(0);
        if (x < getNode(0).x)
            return getNode(count - 1);
        for (int i = 0; i < count - 1; ++i) {
            if (getNode(i).x < x) {
                if (getNode(i + 1).x >= x)
                    return getNode(i);
            }
        }
        return getNode(count - 1);
    }

    // apply. Вообще хз-хз
    @Override
    public double apply(double x) {
        if (x < getX(0))
            return extrapolateLeft(x);
        if (x > getX(getCount() - 1))
            return extrapolateRight(x);
        int index = indexOfX(x);
        if (index != -1)
            return getY(index);
        Node nodeEl = floorNodeOfX(x);
        return interpolate(x, nodeEl);
    }

    // Интерполяция, экстраполяция. Хз-хз
    @Override
    protected double interpolate(double x, int floorIndex) {
        if (count == 1)
            return getY(0);

        if (floorIndex < 0 || floorIndex >= count - 1)
            return Double.NaN;

        double leftX = getX(floorIndex);
        double leftY = getY(floorIndex);
        double rightX = getX(floorIndex + 1);
        double rightY = getY(floorIndex + 1);

        return interpolate(x, leftX, rightX, leftY, rightY);
    }

    protected double interpolate(double x, Node floorNode) {
        if (count == 1)
            return getY(0);

        double leftX = floorNode.x;
        double leftY = floorNode.y;
        double rightX = floorNode.next.x;
        double rightY = floorNode.next.y;

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

    // Метод remove. Хз-хз
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
} */