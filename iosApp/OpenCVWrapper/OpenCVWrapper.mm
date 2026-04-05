// Import OpenCV BEFORE Foundation to avoid NO macro conflict
#ifdef __cplusplus
#import <opencv2/core.hpp>
#import <opencv2/imgcodecs.hpp>
#import <opencv2/imgproc.hpp>
#endif

#import "OpenCVWrapper.h"

@implementation OpenCVWrapper

+ (nullable NSData *)detectEdgesFromData:(NSData *)imageData
                            lowThreshold:(double)lowThreshold
                           highThreshold:(double)highThreshold {
    // Decode NSData to cv::Mat
    std::vector<unsigned char> buffer((unsigned char *)imageData.bytes,
                                      (unsigned char *)imageData.bytes + imageData.length);
    cv::Mat mat = cv::imdecode(buffer, cv::IMREAD_COLOR);
    if (mat.empty()) {
        return nil;
    }

    // Convert to grayscale
    cv::Mat gray;
    cv::cvtColor(mat, gray, cv::COLOR_BGR2GRAY);

    // Apply Canny
    cv::Mat edges;
    cv::Canny(gray, edges, lowThreshold, highThreshold);

    // Encode to PNG
    std::vector<unsigned char> output;
    cv::imencode(".png", edges, output);

    return [NSData dataWithBytes:output.data() length:output.size()];
}

@end
