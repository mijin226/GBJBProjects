package controller.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@MultipartConfig
public class ProfilePicUpload {
	// 기본 프로필명 선언
	private final static String DEFAULTPROFILEPIC = "default.jpg";
	// 서버에 입력받은 파일을 저장할 폴더 경로 선언
	private final static String PATH = "D:\\JJH\\workspace02\\day039\\src\\main\\webapp\\images\\";

	// 파일을 지정 PATH 경로의 폴더에 저장하는 메서드
	public static String profilePicUpload(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("	log : ProfilePicUpload.profilePicUpload()		시작");
		// 사용자가 업로드한 프로필 사진으로 변경하는 부분
		// 사용자가 업로드한 데이터 파일을 담을 객체 생성
		Part file = null;
		String get_profilePic = null;
		try {
			// 데이터 파일을 가져온다.
			file=request.getPart("profilePic");
			System.out.println("	log : profilePicUpload()		profilePic 데이터 가져옴");
			System.out.println("	log : profilePicUpload()		file : "+ file);

			// 만약 파일이 존재한다면
			// 파일 주소를 추출 (파일 이름 추출)
			get_profilePic=Paths.get(file.getSubmittedFileName()).getFileName().toString();

		} catch (Exception e) {
			e.printStackTrace();
			return DEFAULTPROFILEPIC;
		}

		if(!get_profilePic.isEmpty()) {
			System.out.println("	log : profilePicUpload()		이미지 업로드 시작");

			// 파일명이 중복되는 것을 방지하기 위한 부분
			// 파일명에 랜덤한 식별자를 부여
			get_profilePic = UUID.randomUUID().toString() + "_" + get_profilePic;
			System.out.println("	log : profilePicUpload()		랜덤 식별자 부여");
			System.out.println("	log : profilePicUpload()		profilePic : "+ get_profilePic);

			// 파일명에 경로 추가
			// File(디렉토리, 파일명) 객체 생성
			File uploadFile=new File(new File(PATH), get_profilePic);
			System.out.println("	log : profilePicUpload()		경로 추가");
			System.out.println("	log : profilePicUpload()		profilePic : "+ get_profilePic);


			// 이미지 업로드 부분
			try(var inputStream = file.getInputStream()){
				// 파일의 내용을 저장된 경로에 저장
				// 서버에 저장될 파일 입력
				Files.copy(inputStream, uploadFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
				System.out.println("파일 저장 성공: " + uploadFile.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
				try {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "500: 파일 저장 중 오류가 발생했습니다.");
				} catch (IOException e1) {
					e1.printStackTrace();
					System.out.println("	log : profilePicUpload()		기본 프로필 반환");
					return DEFAULTPROFILEPIC;
				}
			}

			// 파일 경로를 클라이언트로 전송
			response.setContentType("text/plain");
			try {
				response.getWriter().write(PATH + "\\" + get_profilePic);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("	log : profilePicUpload()		기본 프로필 반환");
				return DEFAULTPROFILEPIC;
			}
		}
		System.out.println("	log : profilePicUpload()		get_profilePic : " + get_profilePic);
		System.out.println("	log : ProfilePicUpload.profilePicUpload()		종료");
		return get_profilePic;
	}

	// 파일명에 기본 경로로 초기화하는 메서드
	public static String defaultProfilePic() {
		return DEFAULTPROFILEPIC;
	}
	// 파일명에 경로를 추가해주는 메서드
	public static String addPATH(String fileName) {
		return PATH+fileName;
	}
	// 파일 삭제
	public static void deletefile(String profilePic) {
		// preProfilePic 삭제
		if(!profilePic.equals(DEFAULTPROFILEPIC)) {
			Path path = Paths.get(ProfilePicUpload.addPATH(profilePic));
			System.out.println("	log : ProfilePicUpload.profilePicUpload()		삭제할 path : "+ path);

			try {
				// 해당 경로의 파일을 삭제함
				Files.delete(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("	log : JoinAction.java		프로필 삭제");
		}
	}
}