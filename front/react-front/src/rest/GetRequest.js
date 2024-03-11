import { useState, useCallback } from 'react';

const useGetRequest = (API_URL) => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const getData = useCallback(async (request) => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(`${API_URL}${request}`, {
        credentials: 'include'
      });
      const requestData = await response.json();
      setData(requestData);
    } catch (error) {
      setError(error);
      setData(null);
    } finally {
      setLoading(false);
    }
  }, [API_URL]);

  return { data, setData, loading, setLoading, error, setError, getData };
};

export default useGetRequest;
