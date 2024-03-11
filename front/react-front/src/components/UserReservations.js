import { useState } from 'react';
import GetRequest from '../rest/GetRequest';

const API_URL = process.env.REACT_APP_API_URL

const UserReservations = () => {
  const [username, setUsername] = useState('');
  const { data, setData, loading, error, setError, getData } = GetRequest(API_URL)

  const handleButtonClick = () => {
    if (username !== ''){
      getData(`/users/${username}/reservations`)
    } else {
      setError(new Error("Username must not be empty"));
      setData(null);
    }
  };

  return (
    <div>
        <p>GET /users/{`{name}`}/reservations</p>
        <p>username</p>
        <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
        />
        <p><button onClick={handleButtonClick}>Fetch Data</button></p>
        <div>
          {loading && <p>Loading...</p>}
          {error && <p>{error.message}</p>}
          {data && <pre>{JSON.stringify(data, null, 2)}</pre>}
        </div>
    </div>
  );
};

export default UserReservations;