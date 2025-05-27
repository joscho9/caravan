import ReactMarkdown from 'react-markdown';
import './CaravanDescription.css';

export default function CaravanDescription({ description }) {
    return (
        <section className="caravan-description">
            <h3>Detaillierte Beschreibung</h3>
            <div className="markdown-description">
                <ReactMarkdown>
                    {description}
                </ReactMarkdown>
            </div>
        </section>
    );
}