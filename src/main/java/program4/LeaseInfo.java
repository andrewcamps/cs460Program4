package program4;
/*
Authors: Garrett Seale, Andrew Camps
CSC460
Program 4
LeaseInfo.java: Holds the lease attributes to be returned in the leaseInfo query/page
*/
public class LeaseInfo {

	public int firstyear_hall;
	public int firstyear_apt;
	public int undergrad_hall;
	public int undergrad_apt;
	public int grad_hall;
	public int grad_apt;

	LeaseInfo(int firstyear_hall, int firstyear_apt, int undergrad_hall, int undergrad_apt, int grad_hall, int grad_apt) {
		this.firstyear_hall = firstyear_hall;
		this.firstyear_apt = firstyear_apt;
		this.undergrad_hall = undergrad_hall;
		this.undergrad_apt = undergrad_apt;
		this.grad_hall = grad_hall;
		this.grad_apt = grad_apt;
	}
}
