package org.primefaces.apollo.dto;

import java.io.Serializable;

public class Car implements Serializable {
	private String stt;
	private String tenCoSoSanXuat;
	private String nguoiDaiDien;
	private String diaChi;
	private String dienThoai;
	private String website;
	private String thuDienTu;
	
	public Car(String stt, String tenCoSoSanXuat, String nguoiDaiDien, String diaChi, String dienThoai,
			String website, String thuDienTu) {
		super();
		this.stt = stt;
		this.tenCoSoSanXuat = tenCoSoSanXuat;
		this.nguoiDaiDien = nguoiDaiDien;
		this.diaChi = diaChi;
		this.dienThoai = dienThoai;
		this.website = website;
		this.thuDienTu = thuDienTu;
	}
	public String getStt() {
		return stt;
	}
	public void setStt(String stt) {
		this.stt = stt;
	}
	public String getTenCoSoSanXuat() {
		return tenCoSoSanXuat;
	}
	public void setTenCoSoSanXuat(String tenCoSoSanXuat) {
		this.tenCoSoSanXuat = tenCoSoSanXuat;
	}
	public String getNguoiDaiDien() {
		return nguoiDaiDien;
	}
	public void setNguoiDaiDien(String nguoiDaiDien) {
		this.nguoiDaiDien = nguoiDaiDien;
	}
	public String getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
	public String getDienThoai() {
		return dienThoai;
	}
	public void setDienThoai(String dienThoai) {
		this.dienThoai = dienThoai;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getThuDienTu() {
		return thuDienTu;
	}
	public void setThuDienTu(String thuDienTu) {
		this.thuDienTu = thuDienTu;
	}
}
