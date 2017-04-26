package project1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class Job1KeyWritable implements WritableComparable<Job1KeyWritable> {

	private int month;
	private int year;
    private Text productId;

    public Job1KeyWritable() {
    }

    public Job1KeyWritable(int month, int year, Text productId) {
        this.month = month;
        this.year = year;
        this.productId = productId;
    }

    public void readFields(DataInput in) throws IOException {
    	month = new Integer(in.readUTF());
    	year = new Integer(in.readUTF());
    	productId = new Text(in.readUTF());
    }

    public void write(DataOutput out) throws IOException {
        out.writeUTF(String.valueOf(month));
        out.writeUTF(String.valueOf(year));
        out.writeUTF(productId.toString());
    }

    public void set(int month, int year, Text productId) {
        this.month = month;
        this.year = year;
        this.productId = productId;
    }

    @Override
	public String toString() {
		return month + "\t" + year + "\t" + productId;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Text getProductId() {
		return productId;
	}

	public void setProductId(Text productId) {
		this.productId = productId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + month;
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Job1KeyWritable other = (Job1KeyWritable) obj;
		if (month != other.month)
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (year != other.year)
			return false;
		return true;
	}

	public int compareTo(Job1KeyWritable arg0) {
        if(year < arg0.year) return -1;
        else if(year > arg0.year) return 1;
		if(month < arg0.month) return -1;
        else if(month > arg0.month) return 1;
		return productId.compareTo(arg0.productId);
    }

}
