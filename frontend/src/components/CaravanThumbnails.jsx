function CaravanThumbnails({ caravan }) {
    return (
        <>
            {caravan.images.map((image, index) => (
                <img
                    key={image.id || index}
                    src={`http://localhost:8080/uploads/${image.filePath}`}
                    alt={image.description}
                    style={{ maxWidth: '300px', maxHeight: '200px', margin: '10px' }}
                />
            ))}
        </>
    );
}

export default CaravanThumbnails;
