import UIKit
import shared

class ViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {

    private let originalImageView = UIImageView()
    private let edgesImageView = UIImageView()
    private let slider = UISlider()
    private let pickButton = UIButton(type: .system)
    private let edgeDetector = EdgeDetector()
    private var originalImageData: Data?

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        setupUI()
    }

    private func setupUI() {
        pickButton.setTitle("Pick Image", for: .normal)
        pickButton.addTarget(self, action: #selector(pickImage), for: .touchUpInside)

        slider.minimumValue = 1
        slider.maximumValue = 200
        slider.value = 50
        slider.addTarget(self, action: #selector(sliderChanged), for: .valueChanged)

        originalImageView.contentMode = .scaleAspectFit
        edgesImageView.contentMode = .scaleAspectFit

        let imageStack = UIStackView(arrangedSubviews: [originalImageView, edgesImageView])
        imageStack.axis = .horizontal
        imageStack.distribution = .fillEqually
        imageStack.spacing = 8

        let mainStack = UIStackView(arrangedSubviews: [pickButton, slider, imageStack])
        mainStack.axis = .vertical
        mainStack.spacing = 12
        mainStack.translatesAutoresizingMaskIntoConstraints = false

        view.addSubview(mainStack)
        NSLayoutConstraint.activate([
            mainStack.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            mainStack.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            mainStack.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            mainStack.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -16),
        ])
    }

    @objc private func pickImage() {
        let picker = UIImagePickerController()
        picker.delegate = self
        picker.sourceType = .photoLibrary
        present(picker, animated: true)
    }

    @objc private func sliderChanged() {
        processEdges()
    }

    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey: Any]) {
        dismiss(animated: true)
        guard let image = info[.originalImage] as? UIImage,
              let data = image.pngData() else { return }
        originalImageData = data
        originalImageView.image = image
        processEdges()
    }

    private func processEdges() {
        guard let data = originalImageData else { return }
        let threshold = Double(slider.value)
        let bytes = [UInt8](data)
        let kotlinBytes = KotlinByteArray(size: Int32(bytes.count))
        for (i, byte) in bytes.enumerated() {
            kotlinBytes.set(index: Int32(i), value: Int8(bitPattern: byte))
        }

        let resultBytes = edgeDetector.detectEdges(
            imageBytes: kotlinBytes,
            lowThreshold: threshold,
            highThreshold: threshold * 3
        )

        let length = resultBytes.size
        var swiftBytes = [UInt8](repeating: 0, count: Int(length))
        for i in 0..<length {
            swiftBytes[Int(i)] = UInt8(bitPattern: resultBytes.get(index: i))
        }

        if let resultImage = UIImage(data: Data(swiftBytes)) {
            edgesImageView.image = resultImage
        }
    }
}
