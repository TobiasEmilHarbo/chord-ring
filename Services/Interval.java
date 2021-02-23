package Services;

public class Interval {
  private boolean _wrapsAroundZero;
  private IntervalType _type;
  private int _start;
  private int _end;

  private Interval(int start, int end, IntervalType type) {
    this._start = start;
    this._end = end;
    this._type = type;
    this._wrapsAroundZero = this._start >= this._end;
  }

  public static Interval asOpen(int start, int end) {
    return new Interval(start, end, IntervalType.OPEN);
  }

  public static Interval withClosedStart(int start, int end) {
    return new Interval(start, end, IntervalType.START_HALF_CLOSED);
  }

  public static Interval withClosedEnd(int start, int end) {
    return new Interval(start, end, IntervalType.END_HALF_CLOSED);
  }

  public boolean includes(int id) {
    switch (this._type) {
      case OPEN:
        if(this._wrapsAroundZero) {
          return (this._start < id && id <= 256 || 0 <= id && id < this._end);
        } else {
          return (this._start < id && id < this._end);
        }
      case START_HALF_CLOSED:
        if(this._wrapsAroundZero) {
          return (this._start <= id && id <= 256 || 0 <= id && id < this._end);
        } else {
          return (this._start <= id && id < this._end);
        }
      case END_HALF_CLOSED:
        if(this._wrapsAroundZero) {
          return (this._start < id && id <= 256 || 0 <= id && id <= this._end);
        } else {
          return (this._start < id && id <= this._end);
        }
      default:
        return false;
    }
  }
}
