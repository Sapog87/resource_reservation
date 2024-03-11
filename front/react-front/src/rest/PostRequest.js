import { useState, useCallback } from 'react';

const usePostRequest = (initialFormData, API_URL) => {
  const [formData, setFormData] = useState(initialFormData);
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const postData = useCallback(async (request) => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(`${API_URL}${request}`, {
        credentials: 'include',
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });
      const responseData = await response.json();
      setData(responseData);
    } catch (error) {
      setError(error);
      setData(null);
    } finally {
      setLoading(false);
    }
  }, [formData, API_URL]);

  return { formData, data, setData, loading, setLoading, error, setError, setFormData, postData };
};

export default usePostRequest;
