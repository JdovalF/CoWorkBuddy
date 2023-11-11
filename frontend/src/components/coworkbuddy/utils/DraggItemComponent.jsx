
import { useDrag } from "react-dnd";
import React from 'react';

const DraggItemComponent = ({ worker }) => {
    console.log('Worker:', worker);
    console.log("Entrando al componente de arrastre");
  
    const [, drag] = useDrag({
      type: 'WORKER',
      item: { type: 'WORKER', id: worker.id, name: worker.name, active: worker.active }
    });
    // chip
    // { border: '1px solid #ccc', padding: '8px', margin: '4px', cursor: 'move' }
    return (
      <div ref={drag} 
      className="chip-worker"
      // style={{ border: '1px solid #ccc', padding: '8px', margin: '4px', cursor: 'move' }}
      >
        {worker.name}
      </div>
    );
  }
  
  export default DraggItemComponent;