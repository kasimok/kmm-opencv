#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface OpenCVWrapper : NSObject

/// Applies Canny edge detection to PNG/JPEG image data.
/// Returns the result as PNG-encoded NSData, or nil on failure.
+ (nullable NSData *)detectEdgesFromData:(NSData *)imageData
                            lowThreshold:(double)lowThreshold
                           highThreshold:(double)highThreshold;

@end

NS_ASSUME_NONNULL_END
