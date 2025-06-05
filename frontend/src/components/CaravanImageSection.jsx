import React, { useEffect, useRef } from "react";
import PhotoSwipeLightbox from 'photoswipe/lightbox';
import 'photoswipe/style.css';
import CaravanThumbnails from "./CaravanThumbnails.jsx";


const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api";


export default function CaravanImageSection({ caravan }) {
    const lightboxRef = useRef(null);

    useEffect(() => {
        const lightbox = new PhotoSwipeLightbox({
            gallery: '#caravan-gallery',
            children: 'a',
            pswpModule: () => import('photoswipe'),
        });

        lightbox.init();
        lightboxRef.current = lightbox;

        return () => {
            lightbox.destroy();
            lightboxRef.current = null;
        };
    }, []);

    return (
        <div className="img_and_thumbnail_container">

            <div className="main_image_container" id="caravan-gallery">
                {caravan.images.map((img, index) => (
                    <a
                        href={`${API_URL}/uploads/${img.filePath}`}
                        data-pswp-width={img.width || 1200}
                        data-pswp-height={img.height || 800}
                        key={index}
                        target="_blank"
                        rel="noreferrer"
                    >
                        {index === 0 && (
                            <img
                                className="main_image"
                                //src={`${API_URL}/uploads/${img.filePath}`}
                                src={`${API_URL}/uploads/${caravan.id}/${caravan.mainImagePath}`}
                                alt={caravan.wohnwagentyp}
                                style={{ cursor: 'pointer' }}
                            />
                        )}
                    </a>
                ))}
            </div>

            <div className="thumbnail_container">
                {caravan.images.map((img, index) => (
                    <a
                        href={`${API_URL}/uploads/${img.filePath}`}
                        data-pswp-width={img.width || 1200}
                        data-pswp-height={img.height || 800}
                        key={index}
                        target="_blank"
                        rel="noreferrer"
                    >
                        <img
                            src={`${API_URL}/uploads/${img.filePath}`}
                            alt={`Thumbnail ${index}`}
                            className="thumbnail"
                            style={{
                                cursor: 'pointer',
                                width: '100px',
                                height: 'auto',
                                marginRight: '5px',
                            }}
                        />
                    </a>
                ))}
            </div>

        </div>
    );
}
