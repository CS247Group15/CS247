public class MetaProgrammer
{
	public static void main(String[] args)
	{
		for(int i= -50; i<50; i++)
		{
			int j = i + 51;
			String zeros = "00";
			if(j<10) {zeros = "000";}
			System.out.println("case " + i + " : iv.setImageResource(R.drawable.slider" + zeros + j + "); break;");
		}
	}
}