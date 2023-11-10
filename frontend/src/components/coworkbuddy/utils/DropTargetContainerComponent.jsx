import React from 'react';
import { useDrop } from "react-dnd"

const DropTargetContainerComponent = ({ onDrop, children }) => {
    const [{isOver}, drop] = useDrop({
        accept: 'WORKER',
        drop: (worker) => onDrop(worker),
        collect: (monitor) => ({
            isOver: !!monitor.isOver(),
        })
    })

    return (
        <div ref={drop} style={{ minHeight: '200px', border: '2px dashed #ccc', padding: '16px' }}>
            {children}
        </div>

    )
}

export default DropTargetContainerComponent
