import { useState } from 'react';
import GetRequest from '../rest/GetRequest';

const API_URL = process.env.REACT_APP_API_URL

const ResourceReservations = () => {
  const [name, setName] = useState('');
  const { data, setData, loading, error, setError, getData } = GetRequest(API_URL)

  const handleButtonClick = () => {
    if (name !== ''){
      getData(`/resources/${name}/reservations`)
    } else {
      setError(new Error("Name must not be empty"));
      setData(null);
    }
  };

  return (
    <div>
      <p>Поиск резервов по имени ресурса</p>
      <p>GET /resources/{`{name}`}/reservations</p>
      <p>name of resource</p>
      <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
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

export default ResourceReservations;