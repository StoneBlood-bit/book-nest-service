package mate.academy.service.image;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.model.Book;
import mate.academy.repository.book.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final BookRepository bookRepository;

    public byte[] convertToBytes(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert MultipartFile to byte[]", e);
        }
    }

    public byte[] updateImage(MultipartFile newImage, byte[] existingImage) {
        return (newImage != null && !newImage.isEmpty()) ? convertToBytes(newImage) : existingImage;
    }

    public byte[] getImage(Long bookId) {
        return bookRepository.findById(bookId)
                .map(Book::getImage)
                .orElseThrow(
                        () -> new EntityNotFoundException("Book not found with id: " + bookId)
                );
    }
}
