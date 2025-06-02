function CaravanThumbnails({ caravan }) {
    return (
        <>
            {caravan.images.map((image, index) => (
                <img
                    key={image.id || index}
                    src={`${process.env.REACT_APP_API_URL}/uploads/${image.filePath}`}
                    alt={image.description}
                    style={{ maxWidth: '300px', maxHeight: '200px', margin: '10px' }}
                />
            ))}
        </>
    );
}

export default CaravanThumbnails;
