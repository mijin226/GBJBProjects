package controller.ajax;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

// multipart에 대한 정의
@MultipartConfig(
		// 해당 크기가 넘으면 디스크의 임시 디렉토리에 저장
		fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
		// 파일의 최대 크기
		maxFileSize = 1024 * 1024 * 10,       // 10 MB
		// 한 요청의 최대 크기
		// 여러 파일 및 전송값 등을 합한 최대 크기
		maxRequestSize = 1024 * 1024 * 100   // 100 MB
		)

@WebServlet("/fileUpload")
public class FileUploadAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String UPLOAD_DIRECTORY = "/profilepic/"; // 경로

	public FileUploadAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/fileUpload");
		// 원본 파일명 추출(FileUtil)
		Part part = null;
		try {
			// file 타입으로 전송된 폼 값을 받아 Part객체에 저장
			part = request.getPart("file");
			System.out.println("part : " + part );
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}

		// 만약 받은 파일이 존재한다면
		if (part != null && part.getSize() > 0) {
			// 파일 이름 가져오기
			String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
			System.out.println("fileName : "+fileName);
			
			// 중복 파일명을 피하기 위해 랜덤값을 이름에 부여
			String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
			System.out.println("uniqueFileName : "+uniqueFileName);
			
			// 파일명에 주소를 추가하여 작성
			File file = new File(UPLOAD_DIRECTORY, uniqueFileName);
			System.out.println(file);

			try (var inputStream = part.getInputStream()) {
				// 파일의 내용을 저장된 경로에 저장
				// 기존 파일이 있으면 덮어쓰기
				Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
				System.out.println("파일 저장 성공: " + file.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "500: 파일 저장 중 오류가 발생했습니다.");
				return;
			}

			// 파일 경로를 클라이언트로 전송
			response.setContentType("text/plain");
			response.getWriter().write(UPLOAD_DIRECTORY + "\\" + uniqueFileName);
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "400: 업로드된 파일이 없습니다.");
		}
	}
}