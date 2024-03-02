import { useState } from 'react';
import GetRequest from '../rest/GetRequest';

const API_URL = process.env.REACT_APP_API_URL

const ReservationsById = () => {
  const [id, setId] = useState('');
  const { data, setData, loading, error, setError, getData } = GetRequest(API_URL)

  const handleButtonClick = () => {
    if (id !== ''){
      getData(`/reservations/${id}`)
    } else {
      setError(new Error("Id must not be empty"));
      setData(null);
    }
  };

  return (
    <div>
        <p>/reservations/{`{id}`}</p>
        <p>Enter reservation id</p>
        <input
            type="text"
            value={id}
            onChange={(e) => setId(e.target.value)}
        />
        <button onClick={handleButtonClick}>Fetch Data</button>
        <div>
          {loading && <p>Loading...</p>}
          {error && <p>{error.message}</p>}
          {data && <pre>{JSON.stringify(data, null, 2)}</pre>}
        </div>
    </div>
  );
};

export default ReservationsById;