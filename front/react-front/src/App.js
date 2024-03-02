import './App.css';

import UserReservations from './components/UserReservations'
import ResourceReservations from './components/ResourceReservations'
import ReservationsById from './components/ReservationsById'
import ReservationsByDate from './components/ReservationsByDate'
import Acquire from './components/Acquire'
import Release from './components/Release'

export default function App() {
  return (
    <div>
      <div className="comp"><UserReservations /></div>
      <hr></hr>
      <div className="comp"><ResourceReservations /></div>
      <hr></hr>
      <div className="comp"><ReservationsById /></div>
      <hr></hr>
      <div className="comp"><ReservationsByDate /></div>
      <hr></hr>
      <div className="comp"><Acquire /></div>
      <hr></hr>
      <div className="comp"><Release /></div>
      <hr></hr>
    </div>
  );
}