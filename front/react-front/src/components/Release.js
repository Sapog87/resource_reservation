import { useState } from 'react';
import PostRequest from '../rest/PostRequest';

const API_URL = process.env.REACT_APP_API_URL

const Release = () => {
  const [id, setId] = useState('');

  const { data, setData, loading, error, setError, postData } = PostRequest(
    {},
    API_URL
  );
  
  const handleButtonClick = () => {
    if (id != null){
      postData(`/reservations/release/${id}`);
    } else {
      setError(new Error("Id must not be empty"));
      setData(null);
    }
  };

  return (
    <div>
        <p>POST /reservations/release</p>
        <p>reservation id</p>
        <input
            type="text"
            value={id}
            onChange={(e) => setId(e.target.value)}
        />
        <p><button onClick={handleButtonClick}>Send</button></p>
        <div>
          {loading && <p>Loading...</p>}
          {error && <p>{error.message}</p>}
          {data && <pre>{JSON.stringify(data, null, 2)}</pre>}
        </div>
    </div>
  );
};

export default Release;