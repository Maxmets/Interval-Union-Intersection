import java.util.ArrayList;

public final class IntervalUnion
{
    private ArrayList<Interval> intervalArray;

    /**
     * Internal class that is a set of intervals.
     * Keeps track of start and end value of a set
     */
    class Interval
    {
        private int start;
        private int end;

        public Interval(int start, int end)
        {
            this.start = start;
            this.end = end;
        }

        public boolean hasIntersection(Interval other)
        {
            // [5-15] [7-26]
            return Math.max(this.start, other.start) <= Math.min(this.end, other.end);
        }

        public Interval intersection(Interval other)
        {
            Interval newInterval = new Interval(Math.max(this.start, other.start) , Math.min(this.end, other.end));

            return newInterval;
        }

        public boolean hasUnion(Interval other)
        {
           return hasIntersection(other) || (this.start  == other.end + 1 || this.end == other.start - 1);

        }

        public Interval union(Interval other)
        {
            Interval newInterval = new Interval(Math.min(this.start, other.start) , Math.max(this.end, other.end));
            return newInterval;
        }

    }

    /**
     * Default constructor
     */

    private IntervalUnion()
    {
        this.intervalArray = new ArrayList<Interval>();
    }

    /**
     * Constructor that works with the create method.
     * Creates an array of start and end number.
     * @param start the starting number of the interval
     * @param end the ending number of the interval
     */
    public IntervalUnion(int start, int end)
    {
        intervalArray = new ArrayList<Interval>();
        Interval temp = new Interval(start, end);
        intervalArray.add(temp);
    }

    /**
     * Generates the set
     * @param start the start number
     * @param end   the end number of the set
     * @return an IntervalUnion object that is a set [start-end]
     */
    public static IntervalUnion create(int start , int end)
    {
        IntervalUnion set = new IntervalUnion(start, end);
        return set;
    }

    /**
     * Looks to find the union between two sets
     * @param other a set that is used to compare to another set
     * @return a set that is a union of both sets
     */

    public IntervalUnion union(IntervalUnion other)
    {
        IntervalUnion newSet = new IntervalUnion();

        if(this.intervalArray.size() == 0 && other.intervalArray.size() == 0)
        {
            return newSet;
        }
        int i = 0, j = 0;
        Interval intervalToAdd;

        while(i < this.intervalArray.size() || j < other.intervalArray.size())
        {
            Interval thisInterval = null;
            Interval otherInterval = null;

            if(i < this.intervalArray.size())
            {
                thisInterval = this.intervalArray.get(i);
            }
            if(j < other.intervalArray.size())
            {
                otherInterval = other.intervalArray.get(j);
            }

            if(thisInterval != null && otherInterval != null)
            {
                if (thisInterval.start < otherInterval.start)
                {
                    intervalToAdd = thisInterval;
                    i++;
                }
                else
                {
                    intervalToAdd = otherInterval;
                    j++;
                }
            }
            else
            {
                if(thisInterval != null)
                {
                    intervalToAdd = thisInterval;
                    i++;
                }
                else
                {
                    intervalToAdd = otherInterval;
                    j++;
                }
            }

            Interval lastInterval = null;

            // get the last interval, if exists
            if(newSet.intervalArray.size() > 0) {
                lastInterval = newSet.intervalArray.get(newSet.intervalArray.size() - 1);
            }

            // if there is a last and its mergable with the interval we need to add, then ...
            if(lastInterval != null && lastInterval.hasUnion(intervalToAdd))
            {
                newSet.intervalArray.remove(newSet.intervalArray.size() - 1);
                newSet.intervalArray.add(lastInterval.union(intervalToAdd));
            }
            else
            {
                newSet.intervalArray.add(intervalToAdd);
            }


        }
        return newSet;

    }

    /**
     * Looks to find an intersection between two sets
     * @param other a set that is compared to another set
     * @return a set that is an intersection between both sets
     */

    public IntervalUnion intersection(IntervalUnion other)
    {
        IntervalUnion newSet = new IntervalUnion();

        if(this.intervalArray.size() == 0 || other.intervalArray.size() == 0)
        {
            return newSet;
        }
        int i = 0, j = 0;

        while(i < this.intervalArray.size() && j < other.intervalArray.size())
        {
            Interval thisInterval = this.intervalArray.get(i);
            Interval otherInterval = other.intervalArray.get(j);

            if (thisInterval.hasIntersection(otherInterval)) {
               Interval intersectionInterval = thisInterval.intersection(otherInterval);
               newSet.intervalArray.add(intersectionInterval);
            }
            // [3-10][14-19]
            // [2-8][15-20]

            if(thisInterval.end < otherInterval.end)
            {
                i++;
            }
            else
            {
                j++;
            }

        }

        return newSet;
    }

    /**
     * A string representation of the set
     * @return a string representation of the set
     */
    public String toString()
    {
        StringBuilder set = new StringBuilder();
        set.append("[");
        Interval temp;


        for(int i =0; i < this.intervalArray.size(); i++)
        {
            temp = this.intervalArray.get(i);

            set.append(temp.start);
            if(temp.start < temp.end)
            {
                set.append("-").append(temp.end);
            }

            // add comma if its is not the last one in the array
            if(i < this.intervalArray.size() -1)
            {
                set.append(",");
            }
        }

        set.append("]");

        return set.toString();
    }


    /**
     *
     * @param other Other IntervalUnion object that is being compared to.
     * @return if the sets are equal
     */
    public boolean equals(Object other)
    {
        IntervalUnion otherset = (IntervalUnion) other;

        if(this.intervalArray.size() != otherset.intervalArray.size())
        {
            return false;
        }

        for(int i = 0; i < this.intervalArray.size(); i ++)
        {
            Interval ThisInterval = this.intervalArray.get(i);
            Interval OtherInterval = otherset.intervalArray.get(i);

            if(ThisInterval.start != OtherInterval.start || ThisInterval.end != OtherInterval.end)
            {
                return false;
            }
        }
        return true;
    }

    public int hashCode()
    {
        ArrayList<Integer> hashArray = new ArrayList<>();
        Interval temp;

        for(int i =0; i < this.intervalArray.size(); i++)
        {
            temp = this.intervalArray.get(i);

            hashArray.add(temp.start);
            hashArray.add(temp.end);
        }


        return hashArray.hashCode();
    }

    public boolean contains(int x)
    {
        Interval temp;

        for(int i =0; i < this.intervalArray.size(); i++)
        {
            temp = this.intervalArray.get(i);
            if(x >= temp.start && x <= temp.end)
            {
                return true;
            }
        }
        return false;
    }

    public int getPieceCount()
    {
        return this.intervalArray.size();
    }

    public static String getAuthorName()
    {
        return "Yakhymets, Maksym";
    }

    public static String getRyersonID()
    {
        return "500838023";
    }

}