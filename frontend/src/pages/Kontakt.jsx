import React, { useState } from 'react';
import Header from '../components/Header.jsx';
import Footer from '../components/Footer.jsx';

const API_URL = import.meta.env.VITE_API_URL;

const Kontakt = () => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    subject: '',
    message: ''
  });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitStatus, setSubmitStatus] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    setSubmitStatus(null);

    try {
      const response = await fetch(`${API_URL}/contact`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        setSubmitStatus('success');
        setFormData({ name: '', email: '', subject: '', message: '' });
      } else {
        setSubmitStatus('error');
      }
    } catch (error) {
      console.error('Error submitting form:', error);
      setSubmitStatus('error');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="page-container">
      <Header />
      <div className="kontakt-content">
        <h1>Kontakt</h1>
        <p>
          <strong>Telefon:</strong> +49 157 53048196<br />
          <strong>E-Mail:</strong> uwe.scholz@live.de<br />
        </p>
        <br />
        <br />
        <h1>Nachricht senden</h1>
        <form onSubmit={handleSubmit} className="kontakt-form">
          <div className="form-group">
            <label htmlFor="name">Name *</label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="email">E-Mail *</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="subject">Betreff *</label>
            <input
              type="text"
              id="subject"
              name="subject"
              value={formData.subject}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="message">Nachricht *</label>
            <textarea
              id="message"
              name="message"
              value={formData.message}
              onChange={handleChange}
              required
              rows="6"
            />
          </div>

          <button 
            type="submit" 
            className="submit-btn"
            disabled={isSubmitting}
          >
            {isSubmitting ? 'Wird gesendet...' : 'Nachricht senden'}
          </button>

          {submitStatus === 'success' && (
            <div className="success-message">
              ✅ Ihre Nachricht wurde erfolgreich gesendet! Wir melden uns zeitnah bei Ihnen.
            </div>
          )}

          {submitStatus === 'error' && (
            <div className="error-message">
              ❌ Es gab einen Fehler beim Senden Ihrer Nachricht.
            </div>
          )}
        </form>
      </div>
      <Footer />
    </div>
  );
};

export default Kontakt; 